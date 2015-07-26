;NSIS Modern User Interface version 1.70
;Dts2Admin Installer Script
;Written by Stephen Strenn

;--------------------------------
;Include Modern UI

  !include "MUI.nsh"

;--------------------------------
;General

  ;Name and file
  Name "Dts2Admin"
  OutFile "달구지II 인쇄설정 Installer.exe"

  ;Default installation folder
  InstallDir "$PROGRAMFILES\casmall\Dts2Admin"
  
  ;Get installation folder from registry if available
  InstallDirRegKey HKCU "Software\Dts2Admin" ""

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING
	!define MUI_HEADERIMAGE ".\Dts2AdminInstallerSplash.bmp"
	!define MUI_HEADERIMAGE_BITMAP_NOSTRETCH
	!define MUI_HEADERIMAGE_BITMAP ".\Dts2AdminInstallerSplash.bmp"
	!define MUI_ICON ".\setup.ico"
	!define MUI_UNICON ".\setup.ico"

;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE ".\License.txt"
  !insertmacro MUI_PAGE_COMPONENTS
  !insertmacro MUI_PAGE_DIRECTORY
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_CONFIRM
  !insertmacro MUI_UNPAGE_INSTFILES
  
;--------------------------------
;Languages
 
  !insertmacro MUI_LANGUAGE "English"

;--------------------------------
;Installer Sections

Section "Dts2Admin (required)" SecDummy

  SectionIn RO

  ;Files to be installed
  SetOutPath "$INSTDIR"
  File ".\Dts2Admin.ico"
	File /r ".\Dts2Admin\"
	File /r ".\copy\"

    ; Write the installation path into the registry
  WriteRegStr HKLM SOFTWARE\Dts2Admin "Install_Dir" "$INSTDIR"
  
  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Dts2Admin" "DisplayName" "Dts2Admin"
  WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Dts2Admin" "UninstallString" '"$INSTDIR\uninstall.exe"'
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Dts2Admin" "NoModify" 1
  WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Dts2Admin" "NoRepair" 1
  WriteUninstaller "uninstall.exe"
  
SectionEnd

; Optional section (can be disabled by the user)
Section "Start Menu Shortcuts"
  SetShellVarContext all
  CreateDirectory "$SMPROGRAMS\달구지2\인쇄설정"
  CreateShortCut "$SMPROGRAMS\달구지2\인쇄설정\Uninstall.lnk" "$INSTDIR\uninstall.exe" "" "$INSTDIR\uninstall.exe"
  CreateShortCut "$SMPROGRAMS\달구지2\인쇄설정\달구지2 인쇄설정.lnk" "$INSTDIR\Dts2Admin.exe" "" "$INSTDIR\Dts2Admin.ico"
SectionEnd

;--------------------------------
;Uninstaller Section

Section "Uninstall"

  ; Remove registry keys
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\Dts2Admin"
  DeleteRegKey HKLM SOFTWARE\Dts2Admin
  DeleteRegKey /ifempty HKCU "Software\Dts2Admin"

	; Remove shortcuts
	SetShellVarContext all
  RMDir /r "$SMPROGRAMS\달구지2\인쇄설정"

  ; Remove directories used
  RMDir /r "$INSTDIR"

SectionEnd