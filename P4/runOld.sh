#!/bin/bash

green='\033[0;32m'
red='\033[0;31m'
clear='\033[0m'

cd Run/IRTests
fname=$(ls *.microIR)
cd ../../

javac P4.java

echo "--------------------------------------------------------"

if [[ $# == 0 ]] #Test T0 -> Working Test
then
    echo "Processing T0.microIR"
    name=$(basename T0.microIR .microIR)
    java P4 < Run/IRTests/T0.microIR > Run/RAOut/$name.miniRA
    java -jar pgi.jar < Run/IRTests/$name.microIR 1> Run/ExpOut/$name 2>/dev/null
    java -jar kgi.jar < Run/RAOut/$name.miniRA 1> Run/FinalOut/$name 2>/dev/null
    if [[ $(cat Run/ExpOut/$name) == $(cat Run/FinalOut/$name) ]]
    then
        echo -e "----->" "${green}Accepted${clear}"
        echo "--------------------------------------------------------"
    else
        echo -e "----->" "${red}Wrong Answer${clear} "
        echo "--------------------------------------------------------"
    fi
    exit
fi

if [[ $# == 1 && $1 != "all" ]] #Test Specific file
then

    echo "Processing $1"
    name=$(basename $1 .microIR)
    java P4 < Run/IRTests/$1 > Run/RAOut/$name.miniRA
    java -jar pgi.jar < Run/IRTests/$name.microIR 1> Run/ExpOut/$name 2>/dev/null
    java -jar kgi.jar < Run/RAOut/$name.miniRA 1> Run/FinalOut/$name 2>/dev/null
    if [[ $(cat Run/ExpOut/$name) == $(cat Run/FinalOut/$name) ]]
    then
        echo -e "----->" "${green}Accepted${clear}"
        echo "--------------------------------------------------------"
    else
        echo -e "----->" "${red}Wrong Answer${clear} "
        echo "--------------------------------------------------------"
    fi
    exit
fi

if [[ $1 == "all" ]] #Run and Test all files
then
    for f in $fname
    do
        echo "Processing $f"
        name=$(basename $f .microIR)
        java P4 < Run/IRTests/$f > Run/RAOut/$name.miniRA
        java -jar pgi.jar < Run/IRTests/$name.microIR 1> Run/ExpOut/$name 2>/dev/null
        java -jar kgi.jar < Run/RAOut/$name.miniRA 1> Run/FinalOut/$name 2>/dev/null
        if [[ $(cat Run/ExpOut/$name) == $(cat Run/FinalOut/$name) ]]
        then
            echo -e "----->" "${green}Accepted${clear}"
            echo "--------------------------------------------------------"
        else
            echo -e "----->" "${red}Wrong Answer${clear} "
            echo "--------------------------------------------------------"
        fi
    done
    exit
fi

echo "Bad Argument list"
