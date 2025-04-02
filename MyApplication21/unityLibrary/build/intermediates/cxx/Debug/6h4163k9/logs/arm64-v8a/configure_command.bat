@echo off
"C:\\Users\\COSH\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\cmake.exe" ^
  "-HC:\\Users\\COSH\\AndroidStudioProjects\\MyApplication35\\unityLibrary\\src\\main\\cpp" ^
  "-DCMAKE_SYSTEM_NAME=Android" ^
  "-DCMAKE_EXPORT_COMPILE_COMMANDS=ON" ^
  "-DCMAKE_SYSTEM_VERSION=23" ^
  "-DANDROID_PLATFORM=android-23" ^
  "-DANDROID_ABI=arm64-v8a" ^
  "-DCMAKE_ANDROID_ARCH_ABI=arm64-v8a" ^
  "-DANDROID_NDK=C:\\Program Files\\Unity\\Hub\\Editor\\6000.0.30f1\\Editor\\Data\\PlaybackEngines\\AndroidPlayer\\NDK" ^
  "-DCMAKE_ANDROID_NDK=C:\\Program Files\\Unity\\Hub\\Editor\\6000.0.30f1\\Editor\\Data\\PlaybackEngines\\AndroidPlayer\\NDK" ^
  "-DCMAKE_TOOLCHAIN_FILE=C:\\Program Files\\Unity\\Hub\\Editor\\6000.0.30f1\\Editor\\Data\\PlaybackEngines\\AndroidPlayer\\NDK\\build\\cmake\\android.toolchain.cmake" ^
  "-DCMAKE_MAKE_PROGRAM=C:\\Users\\COSH\\AppData\\Local\\Android\\Sdk\\cmake\\3.22.1\\bin\\ninja.exe" ^
  "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=C:\\Users\\COSH\\AndroidStudioProjects\\MyApplication35\\unityLibrary\\build\\intermediates\\cxx\\Debug\\6h4163k9\\obj\\arm64-v8a" ^
  "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=C:\\Users\\COSH\\AndroidStudioProjects\\MyApplication35\\unityLibrary\\build\\intermediates\\cxx\\Debug\\6h4163k9\\obj\\arm64-v8a" ^
  "-DCMAKE_BUILD_TYPE=Debug" ^
  "-DCMAKE_FIND_ROOT_PATH=C:\\Users\\COSH\\AndroidStudioProjects\\MyApplication35\\unityLibrary\\.cxx\\Debug\\6h4163k9\\prefab\\arm64-v8a\\prefab" ^
  "-BC:\\Users\\COSH\\AndroidStudioProjects\\MyApplication35\\unityLibrary\\.cxx\\Debug\\6h4163k9\\arm64-v8a" ^
  -GNinja ^
  "-DANDROID_STL=c++_shared"
