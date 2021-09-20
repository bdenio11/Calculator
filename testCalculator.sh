#!/bin/sh

# calculate "1 + 2" gives 3
# calculate "4*5/2" gives 10
# calculate "-5+-8--11*2" gives 9
# calculate "-.32       /.5" gives -0.64
# calculate "(4-2)*3.5" gives 7
# calculate "2+-+-4" gives Syntax Error (or similar)
# calculate "19 + cinnamon" gives Invalid Input (or similar)

javac Calculator.java
echo testing Calculator
echo 1+2
java Calculator 1+2
sleep 1
echo 4*5/2
java Calculator 4*5/2
sleep 1
echo -5+-8--11*2
java Calculator -5+-8--11*2
sleep 1
echo -.32       /.5
java Calculator -.32       /.5
sleep 1
echo \"-.32       /.5\"
java Calculator "-.32       /.5"
sleep 1
echo "(4-2)*3.5"
java Calculator "(4-2)*3.5"
sleep 1
echo 2+-+-4
java Calculator 2+-+-4
sleep 1
echo 19 + cinnamon
java Calculator "19 + cinnamon"
sleep 1
echo "9 * 8 * 7 = 504"
java Calculator "9 * 8 * 7"
sleep 1
echo "((1+2) + 3) * 7 = 42"
java Calculator "((1+2) + 3) * 7"
sleep 1
echo "((((2*7) + 3) / 4) + 8) = 12.15"
java Calculator "((((2*7) + 3) / 4) + 8)"
sleep 1
echo "((((((("
java Calculator "((((((("
sleep 1
echo "123"
java Calculator "123"