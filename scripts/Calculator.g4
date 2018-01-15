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
RE    :  're' | 'RE';
IM	  :  'im' | 'IM';
POW   : '^';
EUL   : 'e' | 'E';
      
      

number: INT
      | DOUBLE
      ;

expdegree: ( IMAG  MULT?  realNumber ) | ( realNumber  MULT?  IMAG );
      
realNumber : MINUS number #NegativeNumber 
           | number #PositiveNumber 
		   ;
                 
sqrtComplex: SQRT LPAR expr RPAR 
           | SQRT expr
           ;

opComplex  : op=(RE|IM) expr;

expr: '('expr')' # Parens
    | expr op=('*'|'/') expr # MulDiv
    | expr op=('+'|'-') expr # AddSub
    | sqrtComplex #Sqrt
    | complexNumber # Complex
    | opComplex # Operator
    ;

complexNumber: (((realNumber) MULT? LPAR COS LPAR (realNumber) RPAR PLUS IMAG MULT? SIN LPAR (realNumber)) RPAR RPAR 
				| ((realNumber) MULT? LPAR COS  (realNumber)  PLUS IMAG MULT? SIN  (realNumber) RPAR)) 			#TrigComplex
             | LPAR realNumber sig=('-'| '+') (IMAG MULT? realNumber | realNumber MULT? IMAG) RPAR #RectComplex
             | ((realNumber  MULT? EUL POW LPAR expdegree RPAR ) | ( EUL  POW  LPAR expdegree RPAR  MULT?  realNumber)) #ExpComplex
             | (IMAG MULT? realNumber | realNumber MULT? IMAG) #ImgNumber
             | realNumber #ReNumber
             ;
