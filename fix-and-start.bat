@echo off
chcp 65001 >nul
echo ========================================
echo  问题诊断与修复工具
echo ========================================
echo.

cd /d D:\codes\my_class_project

echo [步骤1] 检查Java环境...
java -version 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未安装Java或未配置环境变量
    pause
    exit /b 1
)
echo.

echo [步骤2] 检查Maven环境...
call mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [错误] 未安装Maven或未配置环境变量
    pause
    exit /b 1
)
echo [OK] Maven已安装
echo.

echo [步骤3] 检查MySQL是否运行...
sc query MySQL >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 未检测到MySQL服务，请确保MySQL已启动
) else (
    echo [OK] MySQL服务存在
)
echo.

echo [步骤4] 测试数据库连接...
mysql -u root -proot -e "SELECT 1" >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 无法使用 root/root 连接MySQL
    echo       请修改 src\main\resources\application.yml 中的数据库密码
) else (
    echo [OK] 数据库连接成功
)
echo.

echo [步骤5] 检查数据库是否存在...
mysql -u root -proot -e "USE campus_lost_found" >nul 2>&1
if %errorlevel% neq 0 (
    echo [警告] 数据库 campus_lost_found 不存在，正在创建...
    mysql -u root -proot -e "CREATE DATABASE IF NOT EXISTS campus_lost_found DEFAULT CHARACTER SET utf8mb4"
    if %errorlevel% equ 0 (
        echo [OK] 数据库已创建
        echo [提示] 正在导入表结构...
        mysql -u root -proot campus_lost_found < database\schema.sql
        if %errorlevel% equ 0 (
            echo [OK] 表结构导入成功
        ) else (
            echo [错误] 表结构导入失败
        )
    )
) else (
    echo [OK] 数据库已存在
)
echo.

echo [步骤6] 清理并编译项目...
call mvn clean compile -q
if %errorlevel% neq 0 (
    echo [错误] 编译失败！正在显示详细错误...
    call mvn compile
    pause
    exit /b 1
)
echo [OK] 编译成功
echo.

echo ========================================
echo  所有检查通过！正在启动项目...
echo ========================================
echo.
echo  启动后请访问: http://localhost:8080
echo  按 Ctrl+C 可停止项目
echo.

call mvn spring-boot:run

pause
