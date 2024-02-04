#!/bin/bash

# 读取当前版本号
current_version=$(cat version.txt)

# 将版本号加一
new_version=$((current_version + 1))

# 将新版本号写入 version.txt 文件
echo $new_version > version.txt

# 执行 git add 和 git commit 命令
git add .
git commit -m "Update to version $new_version"
