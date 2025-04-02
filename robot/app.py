from flask import Flask, request, jsonify, render_template
from flask_cors import CORS 
import openai
import os
import requests
import json
from datetime import datetime

# 設定 OpenAI API 金鑰
openai.api_key = ''
# 設定 Google API 金鑰
GOOGLE_API_KEY = ''

# 初始化 Flask 應用
app = Flask(__name__, template_folder="templates")
CORS(app)

# 設定聊天歷史記錄的 JSON 檔案路徑
CHAT_HISTORY_FILE = 'C:/virtual_bot_project boy/chat_history.json'

# 確保目錄存在
os.makedirs(os.path.dirname(CHAT_HISTORY_FILE), exist_ok=True)

# 讀取聊天歷史記錄
def load_chat_history():
    try:
        if os.path.exists(CHAT_HISTORY_FILE):
            with open(CHAT_HISTORY_FILE, 'r', encoding='utf-8') as file:
                return json.load(file)
        return []
    except Exception as e:
        print(f"讀取聊天歷史記錄時出錯: {e}")
        return []

# 保存聊天歷史記錄
def save_chat_history(history):
    try:
        with open(CHAT_HISTORY_FILE, 'w', encoding='utf-8') as file:
            json.dump(history, file, ensure_ascii=False, indent=4)
        print("聊天歷史記錄已保存")
    except Exception as e:
        print(f"保存聊天歷史記錄時出錯: {e}")

@app.route("/")
def home():
    return render_template("index.html")

@app.route("/chat", methods=["GET"])
def chat():
    try:
        # 取得用戶的訊息和所選性別
        user_input = request.args.get("message", "").strip()
        gender = request.args.get("gender", "male").strip()  # 預設為男生

        if not user_input:
            return jsonify({'error': 'No message provided'}), 400  

        # 呼叫 OpenAI API
        response = openai.ChatCompletion.create(
            model="gpt-4o",
            messages=[{"role": "system", "content": "你是個有禮貌的聊天機器人。"},
                      {"role": "user", "content": user_input}],
            max_tokens=150
        )

        # 取得 OpenAI 回應的內容
        ai_reply = response["choices"][0]["message"]["content"].strip()

        # 將訊息存入 JSON 檔案（包含性別資訊）
        save_message_to_file(user_input, ai_reply, gender)

        # 使用 Google TTS API 生成語音
        audio_data = generate_speech(ai_reply, gender)
        
        return jsonify({
            'response': ai_reply,
            'audio': audio_data,
            'gender': gender
        })
    
    except openai.error.OpenAIError as e:
        return jsonify({'error': f'OpenAI API Error: {str(e)}'}), 500

    except Exception as e:
        return jsonify({'error': f'Unexpected Error: {str(e)}'}), 500

def generate_speech(text, gender="male"):
    """使用 Google TTS API 將文字轉換為語音，並返回 base64 編碼的音頻數據"""
    try:
        url = f"https://texttospeech.googleapis.com/v1/text:synthesize?key={GOOGLE_API_KEY}"
        
        # 根據性別選擇不同的聲音設定
        if gender == "male":
            request_data = {
                "input": {"text": text},
                "voice": {
                    "languageCode": "cmn-TW",
                    "name": "cmn-TW-Wavenet-B",
                    "ssmlGender": "MALE"
                },
                "audioConfig": {
                    "audioEncoding": "MP3",
                    "speakingRate": 1.0,
                    "pitch": 0,
                    "volumeGainDb": 0
                }
            }
        else:  # female
            request_data = {
                "input": {"text": text},
                "voice": {
                    "languageCode": "cmn-TW",  # 中文（台灣）
                    "name": "cmn-TW-Wavenet-A",  # 使用 Wavenet 更自然的聲音
                    "ssmlGender": "FEMALE"  # 女性聲音
                },
                "audioConfig": {
                    "audioEncoding": "MP3",
                    "speakingRate": 1.0,  # 正常語速
                    "pitch": 2,           # 正常音調
                    "volumeGainDb": 0     # 正常音量
                }
            }

        response = requests.post(url, json=request_data)
        
        if response.status_code != 200:
            print(f"Google TTS API 錯誤: {response.text}")
            return None
            
        response_data = response.json()
        return response_data.get("audioContent")
        
    except Exception as e:
        print(f"Google TTS API 錯誤: {str(e)}")
        return None

def save_message_to_file(user_input, ai_reply, gender="male"):
    """將使用者的訊息和 AI 回應存入 JSON 檔案"""
    try:
        # 獲取當前聊天歷史
        chat_history = load_chat_history()
        
        # 添加新的聊天記錄
        new_chat = {
            "timestamp": datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
            "user_message": user_input,
            "bot_response": ai_reply,
            "gender": gender
        }
        
        # 將新記錄添加到歷史列表的開頭（最新的記錄在前）
        chat_history.insert(0, new_chat)
        
        # 保存更新後的歷史記錄
        save_chat_history(chat_history)
        print(f"訊息已存入檔案，性別: {gender}")
    except Exception as e:
        print(f"寫入檔案時出錯: {e}")

@app.route("/history", methods=["GET"])
def get_chat_history():
    """獲取所有聊天記錄"""
    try:
        history = load_chat_history()
        return jsonify(history)
    except Exception as e:
        return jsonify({'error': f'無法獲取聊天記錄: {str(e)}'}), 500

@app.route("/set_gender", methods=["POST"])
def set_gender():
    """設定當前選擇的性別"""
    try:
        data = request.json
        gender = data.get("gender", "male")
        # 這裡可以設置 session 或其他全局變數，這個例子中我們將直接從前端傳遞性別參數
        return jsonify({"success": True, "gender": gender})
    except Exception as e:
        return jsonify({'error': f'設置性別失敗: {str(e)}'}), 500

if __name__ == "__main__":
    app.run(debug=True)