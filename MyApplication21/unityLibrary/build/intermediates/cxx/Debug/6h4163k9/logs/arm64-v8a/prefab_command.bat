@echo off
"C:\\Program Files\\Android\\Android Studio1\\jbr\\bin\\java" ^
  --class-path ^
  "C:\\Users\\COSH\\.gradle\\caches\\modules-2\\files-2.1\\com.google.prefab\\cli\\2.1.0\\aa32fec809c44fa531f01dcfb739b5b3304d3050\\cli-2.1.0-all.jar" ^
  com.google.prefab.cli.AppKt ^
  --build-system ^
  cmake ^
  --platform ^
  android ^
  --abi ^
  arm64-v8a ^
  --os-version ^
  23 ^
  --stl ^
  c++_shared ^
  --ndk-version ^
  23 ^
  --output ^
  "C:\\Users\\COSH\\AppData\\Local\\Temp\\agp-prefab-staging10705512744086216850\\staged-cli-output" ^
  "C:\\Users\\COSH\\.gradle\\caches\\transforms-4\\4a0c3ea794e10e8c73b4fab02d25c52f\\transformed\\jetified-games-frame-pacing-1.10.0\\prefab"
