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

number: INT
      | DOUBLE
      ;
      
      
complexNumber: (number) MULT? LPAR COS LPAR? (number) RPAR? PLUS IMAG MULT? SIN LPAR? (number) RPAR? RPAR;
sqrtComplex: SQRT LPAR expr RPAR; 
reComplex:   RE LPAR expr RPAR;

expr: '('expr')' # Parens
    | expr op=('*'|'/') expr # MulDiv
    | expr op=('+'|'-') expr # AddSub
    | sqrtComplex #Sqrt
    | reComplex #Re
    | complexNumber # Complex
    ;

