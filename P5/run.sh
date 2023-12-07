#!/bin/bash

green='\033[0;32m'
red='\033[0;31m'
clear='\033[0m'
yellow='\033[0;33m'
cyan='\033[0;36m'

if [[ $# == 0 ]]; then
    echo -e "Use -h flag for help"
    exit 1
fi

if [[ $# == 1 ]] && [[ $1 == "-h" ]]; then
    echo "Usage: ./run.sh [flags] <files>"
    echo ""
    echo "flags:"
    echo "-h                show this help message"
    echo "-all              tests all files in RATests"
    echo "-clean            removes all output files in all folders(ExpOut, FinalOut, IROut, RAOut)"
    echo ""
    echo " Note: Just use a single flag at a time"
    exit 
fi

all=0
clean=0
total=0
pass=0
files=()

for it in $(seq 1 $#)
do
    if [[ ${!it} == "-all" ]]; then
        all=1
    elif [[ ${!it} == "-clean" ]]; then
        clean=1
    else
        files+=("${!it}")
        total=$((total+1))
    fi
done


function RATestsToExpOut {
    java -jar Run/JarFiles/R_O.jar < Run/RATests/$1.ra > Run/ExpOut/$1 2>/dev/null
}

function Test {
    java P5 < Run/RATests/$1.ra > Run/AssemblyOut/$1.s 2>/dev/null
    spim -f Run/AssemblyOut/$1.s > Run/Output/$1 2>/dev/null
    sed -i '1,5d' Run/Output/$1
}

function CheckResult {
    if [[ $(cat Run/ExpOut/$1) == $(cat Run/Output/$1) ]]
    then
        echo -e "----->" "${green}Accepted${clear}"
        echo "--------------------------------------------------------"
        pass=$((pass+1))
    else
        echo -e "----->" "${red}Wrong Answer${clear} "
        echo "--------------------------------------------------------"
    fi
}

find . -name "*.class" | xargs rm 2>/dev/null
javac P5.java

if [[ $all == 1 ]]
then
    echo "--------------------------------------------------------"
    echo -e "${yellow}Processing all files in RATests${clear}"
    echo "--------------------------------------------------------"

    cd Run/RATests
    fname=$(ls *.ra)
    cd ../../

    for file in $fname
    do
        total=$((total+1))
        echo "Processing $file"
        file=$(basename $file .ra)
        Test $file
        RATestsToExpOut $file
        CheckResult $file
    done
    echo -e "${yellow}Processing done${clear}"
    echo "--------------------------------------------------------"
    echo -e "${cyan}Result: " $pass "/" $total " passed${clear}"
    echo "--------------------------------------------------------"
    exit
fi

if [[ $clean == 1 ]]
then
    echo "--------------------------------------------------------"
    echo -e "${yellow}Cleaning all files${clear}"
    echo "--------------------------------------------------------"
    rm -rf Run/AssemblyOut/*
    rm -rf Run/ExpOut/*
    rm -rf Run/Output/*
    exit
fi