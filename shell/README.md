# Shell
### 1. Shell介绍  
1. sh \
2. bash \
3. csh \
我们主要是学习bash

### 2. 启动
1. 直接使用路径访问\
2. sh shell.sh(脚本路径)       普通文件、脚本文件   \
3. source shell.sh(脚本路径)   普通文件、脚本文件   \
 1和2 会重新打开一个进程去执行脚本，这个进程是-bash进程的子进程，而-bash进程是该terminal的子进程

### 3. export 命令
首先从当前进程查询变量，如果当前进程没有这个变量，默认去父进程查询这个变量   \
即如果我们使用export命令去修饰一个变量的话，那么这个变量就对子进程是可见的

### 4. Shell 变量
4.1 变量定义不能加美元符号\
4.2 一些其他原则
4.3 变量类型 局部变量，环境变量，Shell变量（前面个连个的混合）
```shell script
name='zhansan'

echo $name

for skill in ada coffe action java; do
  echo "i am good at ${skill} Script"
done

// 只读变量
url="xxxxx"
readonly url
```
### 5. shell的字符串
```shell script
// 字符串操作
str1=zhansan
str2='zhang san'
str3=$str2
echo $str3
str4="$str3 444"
echo $str4
// 单引号不会转义
str5='$str3 555'
echo $str5
// 字符串拼接--双引号
name='sunwukong'
name1="name1 hello,"$name" !"
name2="name2 hello,${name}!"
// 字符串拼接--单引号
name3='name3 hello,'$name' !'
name4='name4 hello,${name}!'  # 这个不会转义
echo $name3
echo $name4
// ${#str} 表示取str字符串的长度
echo ${#str5}
// ${#str5:start:end} 截取部分字符串
echo ${#str5:2:3}
```
### 6. shell的数组
支持一维数组，不支持多维数组
```shell script
#数组的操作
#定义数组
arr=("aaa","bbb","ccc","ddd")
# 访问单个元素
item=${arr[1]}
echo $item
# 访问全部元素
echo ${arr[@]}
# 访问数组长度
len1=${#arr[@]}  // 有问题
len2=${#arr[*]}  // 有问题
echo $len1 + $len2
```
### 7. 注释
```shell script
# 单行注释
# xxx
# 特殊的多行注释
:<<EOFXXX
注释内容
注释内容
注释内容
EOFXXX
:<<!
xxxx
xxxx
!
```

### 8. 运算符
`expr expression `
```shell script
# 运算符
# 注意还不能把“=”使用空白将两边给隔开
a=10
b=20
val1=`expr $a + $b`
echo $val1
val2=`expr 10 + $b`
echo $val2
val3=`expr 10 + 20`
echo $val3

```
### 9. 文件操作
```shell script
# 将结果输出到文件，这个是覆盖
echo "xxxx" > file
# 执行里面的command再 打印输出
echo `command`

```
### 10. test 命令
```shell script
if test $[a] -eq $[b];
then
 echo "相等"
else
 echo "不相等"
fi
```
### 11. 流程控制

```shell script
a=10
b=20
if [ $a == $b ]
then
    echo "do something"
elif [ condition2 ]
    echo "xxx"
else
    echo "do otherthing"
fi

case 值 in
模式1)
  echo "xxx"
;;  # 表示break
模式2)
  echo "xxx"
;;  # 表示break
esac 
  echo "default"
# for循环

for var in item1 item2 item3 ... itemN
do 
  command
  command
done
for var in item1,item2,item3,,,itemN
do 
  command
  command
done

for (( i = 0; i < n; i++ )); do
    command
    command
done
# while 循环，支持break和continue
while [ condition ] 
do
    command
done

while [ condition ]; do
    command
done
```

### 12. 函数
```shell script
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
```
     
     
     