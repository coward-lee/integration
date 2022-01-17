#!/usr/bin/env bash
echo "hello world"
# 变量定义
#name='zhansan'
#echo $name
#for skill in ada coffe action java; do
#  echo "i am good at ${skill} Script"
#done
#
#// 只读变量
#url="xxxxx"
#readonly url
#
#// 字符串操作
#str1=zhansan
#str2='zhang san'
#str3=$str2
#echo $str3
#str4="$str3 444"
#echo $str4
#// 单引号不会转义
#str5='$str3 555'
#echo $str5
#// 字符串拼接--双引号
#name='sunwukong'
#name1="name1 hello,"$name" !"
#name2="name2 hello,${name}!"
#// 字符串拼接--单引号
#name3='name3 hello,'$name' !'
#name4='name4 hello,${name}!'  # 这个不会转义
#echo $name3
#echo $name4
#// ${#str} 表示取str字符串的长度
#echo ${#str5}
#// ${#str5:start:end} 截取部分字符串
#echo ${#str5:2:3}

##数组的操作
##定义数组
#arr=("aaa","bbb","ccc","ddd")
## 访问单个元素
#item=${arr[1]}
#echo $item
## 访问全部元素
#echo ${arr[@]}
## 访问数组长度
#len1=${#arr[@]}  // 有问题
#len2=${#arr[*]}  // 有问题
#echo $len1 + $len2

# 运算符
# 注意还不能把“=”使用空白将两边给隔开
# `expr expression ` 不能解析关系运算符
# 算数
a=10
b=20
#val1=`expr $a + $b`
#echo $val1
#val2=`expr 10 + $b`
#echo $val2
#val3=`expr 10 + 20`
#echo $val3
#echo `expr $a + $b`
#echo `expr $a - $b`
#echo `expr $a \* $b`
#echo `expr $a / $b`
#echo `expr $a % $b`
## 在使用判断的时候需要将==的两边将其隔开
#if [ $a == $b ]; then
#    echo "a 等于 b"
#fi
#if [ $a != $b ]; then
#    echo "a 不等于 b"
#fi
## 关系运算符
## 相等与否
#if [ $a -eq $b ]
#then
#  echo "$a -eq $b: a等于b"
#else
#  echo "$a -eq $b: a不等于b"
#fi
## 大于
#if [ $a -gt $b ]
#then
#  echo "$a -gt $b: a 大于 b"
#else
#  echo "$a -gt $b: a 不大于 b"
#fi
## 小于
#if [ $a -lt $b ]
#then
#  echo "$a -lt $b: a 小于 b"
#else
#  echo "$a -lt $b: a 大于 b"
#fi
#
#if [ ! $a -lt $b ]
#then
#  echo "$a ! $b: a 不小于 b"
#else
#  echo "$a ! $b: a 不大于 b"
#fi
#
#if [ $a -lt $b -o 10 -gt 20 ]
#then
#  echo "-o true"
#else
#  echo "-o false"
#fi
#
#if [ $a -lt $b -a 10 -gt 20 ]
#then
#  echo "-a true"
#else
#  echo "-a false"
#fi
## 短路的与或 需要 [[ expression ]] 需要两组中括号
#if [[ $a -lt $b ||  10 -gt 20 ]]
#then
#  echo "|| true"
#else
#  echo "|| false"
#fi
#
#if [[ $a -lt $b &&  10 -gt 20 ]]
#then
#  echo "&& true"
#else
#  echo "&& false"
#fi
#
#
#if test $[a] -eq $[b];
#then
# echo "相等"
#else
# echo "不相等"
#fi
# 函数
fundDemo(){
  echo "dddd"
}
fundDemo
# 带返回值
funReturn(){
  return 10
}
# 获取返回值
funReturn
# 这个是用来接受函数的返回值的
echo $?
# 带参数的返回值
funParam() {
    echo "param : $1" # 表示第一个参数
    echo "param : $2" # 表示第二个参数
    echo "param : $3" # 表示第三个参数
    echo "param num: $# "
    echo "all params: $*"
}
funParam 1 2 3

sleep 10


