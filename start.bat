@echo off
echo ========================================
echo 启动校园失物招领系统
echo ========================================
echo.

cd /d D:\codes\my_class_project

echo [1] 清理旧文件...
call mvn clean -q

echo [2] 编译项目...
call mvn compile
if %errorlevel% neq 0 (
    echo.
    echo [错误] 编译失败！请检查上面的错误信息
    pause
    exit /b 1
)

echo.
echo [3] 启动项目...
echo.
call mvn spring-boot:run

pause
