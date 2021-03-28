import os
import sys

root=sys.path[0].replace("\\","/")

path_list = []
def get_all(path):
    paths = os.listdir(path)
    for i in paths:
        com_path = "/".join([path,i])
        if os.path.isdir(com_path):
            get_all(com_path)
        elif os.path.isfile(com_path):
            path_list.append(com_path)

get_all(path=root)

print("Java source file compiler\n\ndirectory:\t"+root)

for i in path_list:
    if i.endswith(".java"):
        local=i.split(root+"/")[1]
        
        if(not os.path.exists(i[0:-5]+".class") or os.stat(i[0:-5]+".class").st_mtime<os.stat(i).st_mtime):
            os.system("cd /d "+root.replace("/","\\")+" && javac "+local)
            print("\n\t"+local)

print("\n\n---All java file compiled !---\n\n")