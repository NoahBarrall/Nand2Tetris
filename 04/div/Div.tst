// Div.tst
// David Owen
// For CIS 384 Chapter 4 Assignment.

// load Div.hack,
load Div.asm,

output-file Div.out,
compare-to Div.cmp,
output-list RAM[2]%D2.6.2 RAM[3]%D2.6.2;

set RAM[0] 8;
set RAM[1] 8;
set RAM[2] 0;
set RAM[3] 0;
repeat 500 {
  ticktock;
}
output;

set PC 0,
set RAM[0] 8;
set RAM[1] 4;
set RAM[2] 0;
set RAM[3] 0;
repeat 500 {
  ticktock;
}
output;

set PC 0,
set RAM[0] 0;
set RAM[1] 4;
set RAM[2] 0;
set RAM[3] 0;
repeat 500 {
  ticktock;
}
output;

set PC 0,
set RAM[0] 8;
set RAM[1] 3;
set RAM[2] 0;
set RAM[3] 0;
repeat 500 {
  ticktock;
}
output;

