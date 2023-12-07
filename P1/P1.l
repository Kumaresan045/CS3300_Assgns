%{
    #include "P1.tab.h"
    extern int yyerror(char* s);
%}

%option noyywrap

%%

"//".*\n ;
"/*"("*"[^/]|[^*])*"*/"  ;
"if" {return IF;}
"else" {return ELSE;}
"while" {return WHILE;}
"return" {return RETURN;}
"int" {return INT;}
"void" {return VOID;}
"do" {return DO;}
"main" {return MAIN;}
"public" {return PUBLIC;}
"static" {return STATIC;}
"boolean" {return BOOLEAN;}
"true" {return TRU;}
"false" {return FALS;}
"class" {return CLASS;}
"extends" {return EXTENDS;}
"this" {return THIS;}
"new" {return NEW;}
"String" {return STRING;}
"length" {return LENGTH;}
"System.out.println" {return PRINTLN;}
"#define" {return DEFINE;}

0 {yylval.str = strdup(yytext);return INTLIT;}
[1-9][0-9]* {yylval.str = strdup(yytext);return INTLIT;}
"&&" {return AND;}
"||" {return OR;}
"<=" {return LE;}
">=" {return GE;}
"+" {return PLUS;}
"-" {return MINUS;}
"*" {return TIMES;}
"/" {return DIV;}
"[" {return LBRACK;}
"]" {return RBRACK;}
"!=" {return NE;}
"{"  {return LBRACE;}
"}"  {return RBRACE;}
"("  {return LPAREN;}
")"  {return RPAREN;}
"!" {return NOT;}
";" {return SEMI;}
"," {return COMMA;}
"." {return DOT;}
"=" {return ASSIGN;}

[a-zA-Z_$][a-zA-Z0-9_$]* {yylval.str = strdup(yytext);return ID;}

[ \t\n]+ ;
. {yyerror("");} 
<<EOF>> {return 0;}   

%%