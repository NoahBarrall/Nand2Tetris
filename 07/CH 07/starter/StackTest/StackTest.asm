
    // push constant 17
@17
D=A
@SP
M=M+1
A=M-1
M=D

    // push constant 17
@17
D=A
@SP
M=M+1
A=M-1
M=D

    // eq
@SP
M=M-1
A=M-1
D=M
A=A+1
D=D-M
@FALSE0
D;JNE
@SP
A=M-1
M=-1
@END0
0;JMP
(FALSE0)
@SP
A=M-1
M=0
(END0)

    // push constant 892
@892
D=A
@SP
M=M+1
A=M-1
M=D

    // push constant 891
@891
D=A
@SP
M=M+1
A=M-1
M=D

    // lt
@SP
M=M-1
A=M-1
D=M
A=A+1
D=D-M
@FALSE1
D;JGE
@SP
A=M-1
M=-1
@END1
0;JMP
(FALSE1)
@SP
A=M-1
M=0
(END1)

    // push constant 32767
@32767
D=A
@SP
M=M+1
A=M-1
M=D

    // push constant 32766
@32766
D=A
@SP
M=M+1
A=M-1
M=D

    // gt
@SP
M=M-1
A=M-1
D=M
A=A+1
D=D-M
@FALSE2
D;JLE
@SP
A=M-1
M=-1
@END2
0;JMP
(FALSE2)
@SP
A=M-1
M=0
(END2)

    // push constant 56
@56
D=A
@SP
M=M+1
A=M-1
M=D

    // push constant 31
@31
D=A
@SP
M=M+1
A=M-1
M=D

    // push constant 53
@53
D=A
@SP
M=M+1
A=M-1
M=D

    // add
@SP
A=M-1
D=M
A=A-1
M=D+M
@SP
M=M-1

    // push constant 112
@112
D=A
@SP
M=M+1
A=M-1
M=D

    // sub
@SP
A=M-1
D=M
A=A-1
M=M-D
@SP
M=M-1

    // neg
@SP
A=M-1
M=-M

    // and
@SP
A=M-1
D=M
A=A-1
M=D&M
@SP
M=M-1

    // push constant 82
@82
D=A
@SP
M=M+1
A=M-1
M=D

    // or
@SP
A=M-1
D=M
A=A-1
M=D|M
@SP
M=M-1
