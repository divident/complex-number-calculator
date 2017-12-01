grammar Calculator;

INT    : [0-9]+;
DOUBLE : [0-9]+'.'[0-9]+;

WS     : [ \t\r\n]+ -> skip;


PLUS  : '+';
MINUS : '-';
MULT  : '*';
DIV   : '/';
SIN   : 'sin';
COS   : 'cos';
LPAR  : '(';
RPAR  : ')';
IMAG  : 'i';
SQRT  : 'sqrt';
RE    :  're';
      
      
complexNumber: (number) MULT? LPAR COS LPAR? (number) RPAR? PLUS IMAG MULT? SIN LPAR? (number) RPAR? RPAR #TrigComplex
             | LPAR realNumber sig=('-'| '+') (IMAG MULT? realNumber | realNumber MULT? IMAG) RPAR #RectComplex
             | (IMAG MULT? realNumber | realNumber MULT? IMAG) #ImgNumber
             ;

number: INT
      | DOUBLE
      ;
      
realNumber : MINUS number #NegativeNumber 
           | number #PositiveNumber 
		   ;
                 
sqrtComplex: SQRT LPAR expr RPAR 
           | SQRT expr
           ;

expr: '('expr')' # Parens
    | expr op=('*'|'/') expr # MulDiv
    | expr op=('+'|'-') expr # AddSub
    | sqrtComplex #Sqrt
    | complexNumber # Complex
    ;

