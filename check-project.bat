@echo off
echo ========================================
echo 项目诊断工具
echo ========================================
echo.

echo [1/5] 检查Java版本...
java -version 2>&1
echo.

echo [2/5] 检查Maven版本...
call mvn -version 2>&1 | findstr "Maven home"
echo.

echo [3/5] 检查项目结构...
if exist "src\main\java\com\wyr\my_class_project\MyClassProjectApplication.java" (
    echo [OK] 启动类存在
) else (
    echo [ERROR] 启动类不存在
)

if exist "src\main\resources\application.yml" (
    echo [OK] 配置文件存在
) else (
    echo [ERROR] 配置文件不存在
)

if exist "database\schema.sql" (
    echo [OK] 数据库脚本存在
) else (
    echo [ERROR] 数据库脚本不存在
)
echo.

echo [4/5] 检查MySQL连接...
mysql -u root -proot -e "SELECT 1" >nul 2>&1
if %errorlevel% equ 0 (
    echo [OK] MySQL连接成功 (用户名:root, 密码:root)
) else (
    echo [WARNING] MySQL连接失败，请检查用户名密码
    echo     尝试: mysql -u root -p
)
echo.

echo [5/5] 检查数据库是否存在...
mysql -u root -proot -e "USE campus_lost_found; SHOW TABLES;" 2>nul
if %errorlevel% equ 0 (
    echo [OK] 数据库campus_lost_found存在
) else (
    echo [WARNING] 数据库campus_lost_found不存在
    echo     请运行: init-database.bat
)
echo.

echo ========================================
echo 诊断完成
echo ========================================
echo.
echo 如果MySQL连接失败，请:
echo 1. 确认MySQL服务已启动
echo 2. 修改 src/main/resources/application.yml 中的密码
echo 3. 运行 init-database.bat 初始化数据库
echo.
pause
