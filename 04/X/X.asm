   
   //Written by David Owen and Noah Barrall
        //Starter code provided by Dr. Owen
   
    // M[SCREEN] = 3
    @3
    D=A
    @SCREEN
    M=D
    
    
// M[SCREEN + 32] = 12
    
    // temp = SCREEN + 32
    @SCREEN
    D=A
    @32
    D=D+A
    @temp
    M=D
    
    // D = 12
    @12 
    D=A
    
    // A = temp
    @temp
    A=M
    
    // M[temp] = D
    M=D
    
    
// M[SCREEN + 64] = 48
    
    // temp = SCREEN + 64
    @SCREEN
    D=A
    @64
    D=D+A
    @temp
    M=D
    
    // D = 48
    @48
    D=A
    
    // A = temp
    @temp
    A=M
    
    // M[temp] = D
    M=D


    //addr=SCREEN
    @addr
    A=D
    @SCREEN
    D=M

    //val=3
    @3
    D=A
    @val
    M=D

    //Initialize i to 0
    @0
    D=A
    @i
    M=D

(LOOP)
    //D=i-32
    @i
    D=M
    @32
    D=D-A

    //Check if d >= 0
    @END
    D;JGE

    //Initialize j to 0
    @0
    D=A
    @j 
    M=D

    //D=j-8
    @j
    D=M
    @8
    D=D-A

    //Check if d >= 0
    @END2
    D;JGE

(LOOP2)
    //increment j
    @j
    M=M+1

    //Back to loop 2
    @LOOP2
    0;JMP


    //increment i
    @i
    M=M+1

    //Jump back to loop
    @LOOP
    0;JMP

    //M[addr]=val
    @val
    D=M
    @addr
    A=M
    M=D

    //addr += 32
    @32
    D=A
    @addr
    M=M+D

    //val *= 4
    @val
    D=M
    D=M+D
    D=M+D
    D=M+D
(END2)

    //addr+=1
    @addr
    M=M+1

    //val=3
    @3
    D=A
    @val
    M=D

    

    


