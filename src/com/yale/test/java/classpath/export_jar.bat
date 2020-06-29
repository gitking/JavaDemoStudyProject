@echo off
setlocal enabledelayedexpansion
echo --------------------------------导出jar包批处理工具--------------------------------
echo 说明1:请在当前bat文件目录下放置jar_list.txt清单
echo 说明2:根据jar_list.txt将会生成export_file.txt(自动追加相关匿名内部类)
echo 说明3:最终会以export_file.txt为标准执行导出
echo 说明4:成功为[绿色]背景,失败为[红色]背景
echo 即将开始,请输入您的项目名(如policy,nonvhl,finarp,reinsure,isc,vch,claim7,commbase)↓
set /p inputName=
echo !inputName!
set exportJarName=%~n0
set char1=$&set char2=*
set Dyy=%date:~0,4%& set DMM=%date:~5,2%& set Ddd=%date:~8,2%
type nul>export_file.txt
set export_file=%~dp0export_file.txt
echo 重置export_file.txt文件内容
echo 追加export_file.txt文件内容开始
for /f %%a in (jar_list.txt) do (
	Rem echo %%~nxa
	call :count countResult %%~nxa
	set /a countInt=!countResult!*-1
	echo %%~a>>%export_file%
	set name=%%~na
	if %%~xa == ^.class (
		call :length lengthResult %%a !countInt!
		pushd %cd%
		cd %%~da%%~pa
		set pattern=!name!%char1%%char2%%%~xa
		Rem echo !pattern!
		for %%c in (!pattern!) do (
			echo 发现遗漏文件!lengthResult!%%c,已追加到export_file.txt
			echo !lengthResult!%%c>>%export_file%
		)
		popd
	) else (
		echo ''>nul
	)
)
echo 追加export_file.txt文件内容结束
echo 将以export_file.txt为标准执行jar包导出
jar -cvf !inputName!%Dyy%%DMM%%Ddd%01.jar @%export_file%
if %errorlevel% equ 0 ( 
	echo 导出最终jar包:!inputName!%Dyy%%DMM%%Ddd%01.jar 成功
	color 2f
) else ( 
	echo 导出最终jar包:!inputName!%Dyy%%DMM%%Ddd%01.jar 错误
	color 4f
)

goto exit

:count
set /a num=0
set countArg2=%2
:innercount
if not "%countArg2%"=="" (
	set /a num+=1
	set countArg2=%countArg2:~1%
	goto innercount
)
set %1=%num%
goto :eof

:length
  set lengthArg2=%2
  set lengthArg3=%3
  set o=!lengthArg2:~0,%lengthArg3%!
  Rem echo %2%3!o!
  set %1=!o!
goto :eof

:exit
pause