javac P2.java
rm -rf output.txt

for i in inputs/*/*.java; do
    echo $i
    echo $i: >> output.txt
    echo -ne "\t" >> output.txt
    java P2 < "$i" >> output.txt
    echo >> output.txt
done
