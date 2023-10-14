// Div.asm
// For CIS 384 chapter 4 assignment (part 1).

// Divides R0 by R1; stores quotient in R2 and remainder in R3.
// (R0, R1, R2, R3 refer to RAM[0], RAM[1], RAM[2] and RAM[3].)

//Written by Noah Barrall

// Put your code here.

    // R2 = 0
    @R2
    M=0

    // R3 = R0
    @R0
    D=M
    @R3
    M=D

(LOOP)
    //if R3 < R1 goto END
    @R3
    D=M
    @R1
    D=D-M
    @END
    D;JLT

    // R3 -= R1
    @R1
    D=M
    @R3
    M=M-D

    // R2 += 1
    @R2
    M=M+1

    // goto LOOP
    @LOOP
    0;JMP

(END)

    @END
    0;JMP




     

