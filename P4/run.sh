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
    echo "-ia               tests all IR files in IRTests folder"
    echo "-ja               tests all java files in JavaTests folder"
    echo "-i                tests specific IR file(s), tests T0.microIR by default if no file is specified, For eg: ./run.sh -i T1.microIR"
    echo "-j                tests specific java file(s), tests J0.java by default if no file is specified,  For eg: ./run.sh -j T1.java"
    echo "-all              tests all files in IRTests and JavaTests folder"
    echo "-clean            removes all output files in all folders(ExpOut, FinalOut, IROut, RAOut)"
    echo ""
    echo " Note: Just use a single flag at a time"
    exit 
fi

all=0
ia=0
ja=0
i=0
j=0
clean=0
total=0
pass=0
files=()

for it in $(seq 1 $#)
do
    if [[ ${!it} == "-all" ]]; then
        all=1
    elif [[ ${!it} == "-ia" ]]; then
        ia=1
    elif [[ ${!it} == "-ja" ]]; then
        ja=1
    elif [[ ${!it} == "-i" ]]; then
        i=1
    elif [[ ${!it} == "-j" ]]; then
        j=1
    elif [[ ${!it} == "-clean" ]]; then
        clean=1
    else
        files+=("${!it}")
        total=$((total+1))
    fi
done

function javaToExpOut {
    java -jar Run/JarFiles/J_M.jar < Run/JavaTests/$1.java > Run/IROut/$1.microIR 
    java -jar Run/JarFiles/M_O.jar < Run/IROut/$1.microIR >  Run/ExpOut/$1
}

function IRTestsToExpOut {
    java -jar Run/JarFiles/M_O.jar < Run/IRTests/$1.microIR > Run/ExpOut/$1 2>/dev/null
}

function TestIROut {
    java P4 < Run/IROut/$1.microIR > Run/RAOut/$1.miniRA 2>/dev/null
    java -jar Run/JarFiles/R_O.jar < Run/RAOut/$1.miniRA > Run/FinalOut/$1 2>/dev/null
}

function TestIRTests {
    java P4 < Run/IRTests/$1.microIR > Run/RAOut/$1.miniRA 2>/dev/null
    java -jar Run/JarFiles/R_O.jar < Run/RAOut/$1.miniRA > Run/FinalOut/$1 2>/dev/null
}

function CheckResult {
    if [[ $(cat Run/ExpOut/$1) == $(cat Run/FinalOut/$1) ]]
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
javac P4.java
if [[ $i == 1 ]]
then
    echo "--------------------------------------------------------"
    count=${#files[@]}

    if [[ $count == 0 ]]
    then
        echo "Processing T0.microIR"
        TestIRTests T0
        IRTestsToExpOut T0
        CheckResult T0
    else
        for file in ${files[@]}
        do
            echo "Processing $file"
            file=$(basename $file .microIR)
            TestIRTests $file
            IRTestsToExpOut $file
            CheckResult $file
        done
    fi
    exit
fi

if [[ $j == 1 ]]
then
    echo "--------------------------------------------------------"
    count=${#files[@]}

    if [[ $count == 0 ]]
    then
        echo "Processing J0.java"
        javaToExpOut J0
        TestIROut J0
        CheckResult J0
    else
        for file in ${files[@]}
        do
            echo "Processing $file"
            file=$(basename $file .java)
            javaToExpOut $file
            TestIROut $file
            CheckResult $file
        done
    fi
    exit
fi

if [[ $all == 1 ]]
then
    echo "--------------------------------------------------------"
    echo -e "${yellow}Processing all files in both IRTests and JavaTests${clear}"
    echo "--------------------------------------------------------"
    echo -e "${yellow}Processing all files in IRTests${clear}"
    echo "--------------------------------------------------------"

    cd Run/IRTests
    fname=$(ls *.microIR)
    cd ../../

    for file in $fname
    do
        total=$((total+1))
        echo "Processing $file"
        file=$(basename $file .microIR)
        TestIRTests $file
        IRTestsToExpOut $file
        CheckResult $file
    done
    echo -e "${yellow}Processing done in IRTests${clear}"
    echo "--------------------------------------------------------"
    echo -e "${yellow}Processing all files in JavaTests${clear}"
    echo "--------------------------------------------------------"

    cd Run/JavaTests
    fname=$(ls *.java)
    cd ../../

    for file in $fname
    do
        total=$((total+1))
        echo "Processing $file"
        file=$(basename $file .java)
        javaToExpOut $file
        TestIROut $file
        CheckResult $file
    done
    echo -e "${yellow}Processing done in JavaTests${clear}"
    echo "--------------------------------------------------------"
    echo -e "${cyan}Result: " $pass "/" $total " passed${clear}"
    echo "--------------------------------------------------------"
    exit
fi

if [[ $ia == 1 ]]
then
    echo "--------------------------------------------------------"
    echo -e "${yellow}Processing all files in IRTests${clear}"
    echo "--------------------------------------------------------"

    cd Run/IRTests
    fname=$(ls *.microIR)
    cd ../../

    for file in $fname
    do
        total=$((total+1))
        echo "Processing $file"
        file=$(basename $file .microIR)
        TestIRTests $file
        IRTestsToExpOut $file
        CheckResult $file
    done
    echo -e "${yellow}Processing done in IRTests${clear}"
    echo "--------------------------------------------------------"
    echo -e "${cyan}Result: " $pass "/" $total " passed${clear}"
    echo "--------------------------------------------------------"
    exit
fi

if [[ $ja == 1 ]]
then
    echo "--------------------------------------------------------"
    echo -e "${yellow}Processing all files in JavaTests${clear}"
    echo "--------------------------------------------------------"

    cd Run/JavaTests
    fname=$(ls *.java)
    cd ../../

    for file in $fname
    do
        total=$((total+1))
        echo "Processing $file"
        file=$(basename $file .java)
        javaToExpOut $file
        TestIROut $file
        CheckResult $file
    done
    echo -e "${yellow}Processing done in JavaTests${clear}"
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
    rm -rf Run/IROut/*
    rm -rf Run/RAOut/*
    rm -rf Run/FinalOut/*
    rm -rf Run/ExpOut/*
    exit
fi