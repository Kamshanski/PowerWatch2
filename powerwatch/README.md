# Module info EN

The module contains application code.
The UI is made on Compose Multiplatform.
The application supports MacOS Intel/Amd. Windows and Linux are not yet supported.

The module contains 2 screens:
* info - Screen with information about the battery status
* warining - Fullscreen screen with warning about low battery

To build the application, you need to call the gradle task `packageDmg` (MacOS)
Before building, you need to put the compiled native battery tracking library (libpowerwatch.dylib) in the resources/{platform} folder. This library is compiled in the `shared-power-jni` module

# Module info RU

Модуль содержит код приложения.
UI сделан на Compose Multiplatform.
Приложение поддерживает MacOS Intel/Amd. Windows и Linux пока не поддержаны.

Модуль содержит 2 экрана:
* info - Экран с информацией о состоянии батареи
* warining - Фулскрин экран с предупреждением о разряжающейся батарее

Для сборки приложения нужно вызвать gradle-таску `packageDmg` (MacOS)
Перед сборкой в папку resources/{platform} необходимо положить скомпилированную нативную библиотеку отслеживания батареи (libpowerwatch.dylib). Эта библиотека компилируется в модуле `shared-power-jni`