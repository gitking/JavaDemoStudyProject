@echo off
setlocal enabledelayedexpansion
echo --------------------------------����jar����������--------------------------------
echo ˵��1:���ڵ�ǰbat�ļ�Ŀ¼�·���jar_list.txt�嵥
echo ˵��2:����jar_list.txt��������export_file.txt(�Զ�׷����������ڲ���)
echo ˵��3:���ջ���export_file.txtΪ��׼ִ�е���
echo ˵��4:�ɹ�Ϊ[��ɫ]����,ʧ��Ϊ[��ɫ]����
echo ������ʼ,������������Ŀ��(��policy,nonvhl,finarp,reinsure,isc,vch,claim7,commbase)��
set /p inputName=
echo !inputName!
set exportJarName=%~n0
set char1=$&set char2=*
set Dyy=%date:~0,4%& set DMM=%date:~5,2%& set Ddd=%date:~8,2%
type nul>export_file.txt
set export_file=%~dp0export_file.txt
echo ����export_file.txt�ļ�����
echo ׷��export_file.txt�ļ����ݿ�ʼ
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
			echo ������©�ļ�!lengthResult!%%c,��׷�ӵ�export_file.txt
			echo !lengthResult!%%c>>%export_file%
		)
		popd
	) else (
		echo ''>nul
	)
)
echo ׷��export_file.txt�ļ����ݽ���
echo ����export_file.txtΪ��׼ִ��jar������
jar -cvf !inputName!%Dyy%%DMM%%Ddd%01.jar @%export_file%
if %errorlevel% equ 0 ( 
	echo ��������jar��:!inputName!%Dyy%%DMM%%Ddd%01.jar �ɹ�
	color 2f
) else ( 
	echo ��������jar��:!inputName!%Dyy%%DMM%%Ddd%01.jar ����
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