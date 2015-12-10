import os
from sys import platform as _platform
import platform
import sys
import datetime
import shutil

def create_directories(MAIN_PATH, IMAGE_CORE_FOLDER):
	if not os.path.exists(MAIN_PATH):
		try:
			os.makedirs(MAIN_PATH)
		except OSError as e:
			print("WARNING: Exception make dir: " + e.filename + " " + e.strerror)
			pass
	if not os.path.exists(IMAGE_CORE_FOLDER):
		try:
			os.makedirs(IMAGE_CORE_FOLDER)
		except OSError as e:
			print("WARNING: Exception make dir: " + e.filename + " " + e.strerror)
			pass
def main():
	#IDENTIFY OS
	if _platform == "linux" or _platform == "linux2":
		OS_SYSTEM = "Linux"
		print("INFO: Linux Distribution: " + linux_distribution())
	elif _platform == "darwin":
		OS_SYSTEM = "Mac"
		print("INFO: Mac Version: " + str(platform.mac_ver()))
	elif _platform == "win32":
		OS_SYSTEM = "Win"
	else:
		print("ERROR: ICAT can not work properly on the OS: " + _platform.platform())
	#SYSTEM INFO
	print("INFO: PYTHON Version: " + sys.version)
	print("INFO: OS Platform: " + platform.platform())
	print("INFO: Machine: " + platform.machine())
	print("INFO: CPU Model: " + platform.processor())
	MAIN_FOLDER = "TEST_EXAMPLE"
	if(OS_SYSTEM =="Mac"):
		USER_NAME = "Testdepartment"
		MAIN_PATH = "/Users/" + USER_NAME + "/Downloads" + "/" + MAIN_FOLDER
	if(OS_SYSTEM =="Win"):
		HD_UNIT = "F:"
		MAIN_PATH = HD_UNIT + "/" + MAIN_FOLDER
		IMAGE_CORE_FOLDER = MAIN_PATH + "/" + "IMAGE_CORE_NAME"
	create_directories(MAIN_PATH, IMAGE_CORE_FOLDER)
	xml_filename = "SuiteResources/results.xml"
	#xunit reference http://reflex.gforge.inria.fr/xunit.html#xunitReport
	suite_start_time = datetime.datetime.now()
	shutil.copy2(xml_filename, "results_"+suite_start_time.strftime('%Y-%m-%d %H_%M_%S').replace(" ","_") +".xml")
	
if __name__ == "__main__":
    main()