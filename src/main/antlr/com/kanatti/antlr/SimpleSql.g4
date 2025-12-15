grammar SimpleSql;

@header {
package com.kanatti.antlr;
}

// Parser Rules
query
    : selectStatement EOF
    ;

selectStatement
    : SELECT columnList FROM tableName whereClause?
    ;

columnList
    : STAR                              # SelectAll
    | column (',' column)*              # SelectColumns
    ;

column
    : IDENTIFIER ('.' IDENTIFIER)?      // Supports table.column or just column
    ;

tableName
    : IDENTIFIER
    ;

whereClause
    : WHERE condition
    ;

condition
    : condition AND condition           # AndCondition
    | condition OR condition            # OrCondition
    | '(' condition ')'                 # ParenCondition
    | column operator value             # ComparisonCondition
    ;

operator
    : '='
    | '!='
    | '<'
    | '>'
    | '<='
    | '>='
    ;

value
    : STRING_LITERAL                    # StringValue
    | NUMBER_LITERAL                    # NumberValue
    | IDENTIFIER                        # IdentifierValue
    ;

// Lexer Rules
SELECT      : S E L E C T ;
FROM        : F R O M ;
WHERE       : W H E R E ;
AND         : A N D ;
OR          : O R ;
STAR        : '*' ;

IDENTIFIER
    : [a-zA-Z_][a-zA-Z_0-9]*
    ;

STRING_LITERAL
    : '\'' (~['\r\n])* '\''
    ;

NUMBER_LITERAL
    : [0-9]+ ('.' [0-9]+)?
    ;

// Skip whitespace
WS
    : [ \t\r\n]+ -> skip
    ;

// Case-insensitive keyword fragments
fragment A : [aA] ;
fragment B : [bB] ;
fragment C : [cC] ;
fragment D : [dD] ;
fragment E : [eE] ;
fragment F : [fF] ;
fragment G : [gG] ;
fragment H : [hH] ;
fragment I : [iI] ;
fragment J : [jJ] ;
fragment K : [kK] ;
fragment L : [lL] ;
fragment M : [mM] ;
fragment N : [nN] ;
fragment O : [oO] ;
fragment P : [pP] ;
fragment Q : [qQ] ;
fragment R : [rR] ;
fragment S : [sS] ;
fragment T : [tT] ;
fragment U : [uU] ;
fragment V : [vV] ;
fragment W : [wW] ;
fragment X : [xX] ;
fragment Y : [yY] ;
fragment Z : [zZ] ;
