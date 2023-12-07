%{
    #include <stdio.h>
    #include <stdlib.h>
    #include <string.h>
    extern int yylex(void); // Flex lexer function
    int yyerror(char* s);
    struct node
    {
        char* str;
        struct node* next;
    };

    struct int_node
    {
        int val;
        struct int_node* next;
    };

    struct int_list
    {
        struct int_node* head;
        struct int_node* tail;
    };

    struct arg_node
    {
        char * arg;
        struct int_list* positions;
        struct arg_node* next;
    };

    struct arg_pos_list
    {
        struct arg_node* head;
        struct arg_node* tail;
    };

    struct list
    {
        struct node* head;
        struct node* tail;
        struct list* next;
    };

    struct macrodef
    {
        char* name;
        struct list* arguments;
        struct arg_pos_list* arg_positions;
        struct list* body;
        struct macrodef* next;
        int no_arguments;
        int flag_expr;
    };


    struct symbol_table
    {
        struct macrodef* head;
        struct macrodef* tail;
    } table = {NULL, NULL};

    struct list_lists
    {
        struct list* head;
        struct list* tail;
    };

    void listadd(struct list* l, char* str);
    void listcat(struct list* l1, struct list* l2);
    struct node* makenode(char* str);
    void listprint(struct list* l);
    struct int_node* makeintnode(int val);
    void intlistadd(struct int_list* l, int val);
    void intlistcat(struct int_list* l1, struct int_list* l2);
    void intlistprint(struct int_list* l);
    struct int_list* newintlist();
    struct list* newlist();
    struct macrodef* makemacrodef(char* name, struct list* arguments, struct list* body, int fl);
    struct arg_node* makeargnode(char* arg, struct int_list* positions);
    struct arg_pos_list* newargposlist();
    void argposlistadd(struct arg_pos_list* l, struct arg_node* n);
    void argposprint(struct arg_pos_list* l);
    void symboltableadd(struct symbol_table* st, struct macrodef* m);
    struct list * unpack(char* name, struct list_lists* args);
    struct list * collapse(struct list_lists* l);
    struct list_lists* newlistlists();
    void listlistsadd(struct list_lists* l, struct list* n);
    void listlistscat(struct list_lists* l1, struct list_lists* l2);
    void listlistsprint(struct list_lists* l);
    struct list * copy(struct list * l);
    void listlistaddstr(struct list_lists* l, char* str);
    void replace(struct list* l1, struct list* l2);
    int gettype(char* str);
%}



%token SEMI COMMA LBRACK RBRACK LPAREN RPAREN LBRACE RBRACE DOT AND 
%token OR NOT NE LE GE PLUS MINUS TIMES DIV ASSIGN

%token IF ELSE WHILE DO RETURN INT VOID PRINTLN MAIN PUBLIC DEFINE
%token STATIC CLASS EXTENDS NEW THIS TRU FALS BOOLEAN STRING LENGTH 

%token ID INTLIT 

%union
{
    struct list* tokenlist;
    char* str;
    struct list_lists* listlists;
}


%type<tokenlist> mainclass manytypid listofargs manymacro macro macrodefexpr manystmt goal
%type<tokenlist> type pexpr expr exprcomma otherstmt stmt matched unmatched
%type<tokenlist> macrodefstmt manytypedecl typedecl manymethodecl methodecl typarg commatyparg 
%type<listlists> arguments;
%type<str> ID INTLIT

%nonassoc U_NOT
%nonassoc AND OR NE LE PLUS MINUS TIMES DIV DOT

%start goal

%%

/* Define Grammar */

goal: manymacro mainclass manytypedecl
    {
        $$ = newlist();
        listcat($$, $1);
        listcat($$, $2);
        listcat($$, $3);
        listprint($$);
    } 
    ;

manymacro: {$$ = newlist();} 
        | manymacro macro {$$ = newlist(); listcat($$, $1); listcat($$, $2);}
        ;

macro: macrodefexpr {$$ = $1;}
    |  macrodefstmt {$$ = $1;}
    ;

macrodefexpr: DEFINE ID LPAREN RPAREN LPAREN expr RPAREN 
            {
                $$ = newlist();
                struct list* l = newlist();
                listadd(l, "(");
                listcat(l, $6);
                listadd(l, ")");
                struct macrodef* m = makemacrodef($2, newlist(), l,1);
                symboltableadd(&table, m);
            }
            | DEFINE ID LPAREN listofargs RPAREN LPAREN expr RPAREN 
            {
                $$ = newlist();
                struct list* l = newlist();
                listadd(l, "(");
                listcat(l, $7);
                listadd(l, ")");
                struct macrodef* m = makemacrodef($2, $4, l,1);
                symboltableadd(&table, m);
            }
            ;

macrodefstmt: DEFINE ID LPAREN RPAREN LBRACE manystmt RBRACE 
            {
                $$ = newlist();
                struct list * l = newlist();
                struct macrodef* m = makemacrodef($2, newlist(), $6,0);
                symboltableadd(&table, m);
            }
            | DEFINE ID LPAREN listofargs RPAREN LBRACE manystmt RBRACE 
            {
                $$ = newlist();
                struct list * l = newlist();
                struct macrodef* m = makemacrodef($2, $4,$7,0);
                symboltableadd(&table, m);
                
            }
        ;

listofargs: ID {$$ = newlist(); listadd($$, $1);}
        | ID COMMA listofargs {$$ = newlist(); listadd($$, $1); listcat($$, $3);}
        ;

manystmt: {$$ = newlist();}
        | stmt manystmt {$$ = newlist(); listcat($$, $1); listcat($$, $2);}
        ;

stmt: matched {$$ = $1;}
    | unmatched {$$ = $1;}
    ;
    
matched: IF LPAREN expr RPAREN matched ELSE matched 
    {
        $$ = newlist();
        listadd($$, "if");
        listadd($$, "(");
        listcat($$, $3);
        listadd($$, ")");
        listcat($$, $5);
        listadd($$, "else");
        listcat($$, $7);
    }
    | otherstmt {$$ = $1;}
    ;

unmatched: IF LPAREN expr RPAREN stmt 
    {
        $$ = newlist();
        listadd($$, "if");
        listadd($$, "(");
        listcat($$, $3);
        listadd($$, ")");
        listcat($$, $5);
    }
    | IF LPAREN expr RPAREN matched ELSE unmatched 
    {
        $$ = newlist();
        listadd($$, "if");
        listadd($$, "(");
        listcat($$, $3);
        listadd($$, ")");
        listcat($$, $5);
        listadd($$, "else");
        listcat($$, $7);
    }
    ;

otherstmt: LBRACE manystmt RBRACE 
    {
        $$ = newlist();
        listadd($$, "{");
        listcat($$, $2);
        listadd($$, "}");
    }
    | PRINTLN LPAREN expr RPAREN SEMI 
    {
        $$ = newlist();
        listadd($$, "System.out.println");
        listadd($$, "(");
        listcat($$, $3);
        listadd($$, ")");
        listadd($$, ";");
    }
    | ID ASSIGN expr SEMI 
    {
        $$ = newlist(); 
        listadd($$, $1); 
        listadd($$, "="); 
        listcat($$, $3); 
        listadd($$, ";");
    }
    | ID LBRACK expr RBRACK ASSIGN expr SEMI 
    {
        $$ = newlist();
        listadd($$, $1);
        listadd($$, "[");
        listcat($$, $3);
        listadd($$, "]");
        listadd($$, "=");
        listcat($$, $6);
        listadd($$, ";");
    }
    | DO stmt WHILE LPAREN expr RPAREN SEMI 
    {
        $$ = newlist();
        listadd($$, "do");
        listcat($$, $2);
        listadd($$, "while");
        listadd($$, "(");
        listcat($$, $5);
        listadd($$, ")");
        listadd($$, ";");
    }
    | WHILE LPAREN expr RPAREN stmt 
    {
        $$ = newlist();
        listadd($$, "while");
        listadd($$, "(");
        listcat($$, $3);
        listadd($$, ")");
        listcat($$, $5);
    }
    | ID LPAREN arguments RPAREN SEMI 
    {

        $$ = unpack($1, $3);
        if(gettype($1))
        {
            printf("// Failed to parse macrojava code.");
        }
    }
    | ID LPAREN RPAREN SEMI
    {
        $$ = unpack($1,newlistlists());
        if(gettype($1))
        {
            printf("// Failed to parse macrojava code.");
        }

    }
    ;   

manytypedecl : {$$ = newlist();}
        | manytypedecl typedecl {$$ = newlist(); listcat($$, $1); listcat($$, $2);}
        ;

typedecl : CLASS ID LBRACE manytypid manymethodecl RBRACE 
        {
            $$ = newlist(); 
            listadd($$, "class"); 
            listadd($$, $2); 
            listadd($$, "{"); 
            listcat($$, $4);
            listcat($$, $5); 
            listadd($$, "}");
        }
        | CLASS ID EXTENDS ID LBRACE manytypid manymethodecl RBRACE 
        {
            $$ = newlist(); 
            listadd($$, "class"); 
            listadd($$, $2); 
            listadd($$, "extends"); 
            listadd($$, $4); 
            listadd($$, "{"); 
            listcat($$, $6);
            listcat($$, $7); 
            listadd($$, "}");
        }
        ;

manytypid : {$$ = newlist();}
        | manytypid type ID SEMI 
        {
            $$ = newlist();
            listcat($$, $1);
            listcat($$, $2);
            listadd($$, $3);
            listadd($$, ";");
        }
        ;

manymethodecl : {$$ = newlist();}
        | manymethodecl methodecl {$$ = newlist(); listcat($$, $1); listcat($$, $2);}
        ;


methodecl: PUBLIC type ID LPAREN RPAREN LBRACE manytypid manystmt RETURN expr SEMI RBRACE 
        {
            $$ = newlist();
            listadd($$, "public");
            listcat($$, $2);
            listadd($$, $3);
            listadd($$, "(");
            listadd($$, ")");
            listadd($$, "{");
            listcat($$, $7);
            listcat($$, $8);
            listadd($$, "return");
            listcat($$, $10);
            listadd($$, ";");
            listadd($$, "}");
        }
        | PUBLIC type ID LPAREN typarg commatyparg RPAREN LBRACE manytypid manystmt RETURN expr SEMI RBRACE 
        {
            $$ = newlist();
            listadd($$, "public");
            listcat($$, $2);
            listadd($$, $3);
            listadd($$, "(");
            listcat($$, $5);
            listcat($$, $6);
            listadd($$, ")");
            listadd($$, "{");
            listcat($$, $9);
            listcat($$, $10);
            listadd($$, "return");
            listcat($$, $12);
            listadd($$, ";");
            listadd($$, "}");
        }
        ;

typarg: type ID {$$ = newlist(); listcat($$, $1); listadd($$, $2);}
    ;

commatyparg: {$$ = newlist();}
        | commatyparg COMMA typarg 
        {
            listcat($$, $1);
            listadd($$, ",");
            listcat($$, $3);
        }
        ;

type: INT LBRACK RBRACK 
    {
        $$ = newlist();
        listadd($$, "int[]");
    }
    | BOOLEAN 
    {
        $$ = newlist();
        listadd($$, "boolean");
    }
    | INT 
    {
        $$ = newlist();
        listadd($$, "int");
    }
    | ID 
    {
        $$ = newlist();
        listadd($$, $1);
    }
    ;

mainclass: CLASS ID 
            LBRACE PUBLIC STATIC VOID MAIN LPAREN STRING LBRACK RBRACK ID RPAREN 
                LBRACE 
                    PRINTLN LPAREN expr RPAREN SEMI 
                RBRACE 
            RBRACE 
            
            {
                $$ = newlist();
                listadd($$, "class");
                listadd($$, $2);
                listadd($$, "{");
                listadd($$, "public");
                listadd($$, "static");
                listadd($$, "void");
                listadd($$, "main");
                listadd($$, "(");
                listadd($$, "String");
                listadd($$, "[");
                listadd($$, "]");
                listadd($$, $12);
                listadd($$, ")");
                listadd($$, "{");
                listadd($$, "System.out.println");
                listadd($$, "(");
                listcat($$, $17);
                listadd($$, ")");
                listadd($$, ";");
                listadd($$, "}");
                listadd($$, "}");
            }
    ;

expr: pexpr AND pexpr                               {$$ = newlist(); listcat($$, $1); listadd($$, "&&"); listcat($$, $3);}
    | pexpr OR  pexpr                               {$$ = newlist(); listcat($$, $1); listadd($$, "||"); listcat($$, $3);}
    | pexpr NE  pexpr                               {$$ = newlist(); listcat($$, $1); listadd($$, "!="); listcat($$, $3);}
    | pexpr LE  pexpr                               {$$ = newlist(); listcat($$, $1); listadd($$, "<="); listcat($$, $3);}
    | pexpr PLUS  pexpr                             {$$ = newlist(); listcat($$, $1); listadd($$, "+"); listcat($$, $3);}
    | pexpr MINUS pexpr                             {$$ = newlist(); listcat($$, $1); listadd($$, "-"); listcat($$, $3);}
    | pexpr TIMES  pexpr                            {$$ = newlist(); listcat($$, $1); listadd($$, "*"); listcat($$, $3);}
    | pexpr DIV  pexpr                              {$$ = newlist(); listcat($$, $1); listadd($$, "/"); listcat($$, $3);}
    | pexpr LBRACK pexpr RBRACK                     {$$ = newlist(); listcat($$, $1); listadd($$, "["); listcat($$, $3); listadd($$, "]");}
    | pexpr DOT LENGTH                              {$$ = newlist(); listcat($$, $1); listadd($$, "."); listadd($$, "length");}
    | pexpr                                         {$$ = newlist(); listcat($$, $1);}
    | pexpr DOT ID LPAREN RPAREN                    {$$ = newlist(); listcat($$, $1); listadd($$, "."); listadd($$, $3); listadd($$, "("); listadd($$, ")");}
    | pexpr DOT ID LPAREN expr exprcomma RPAREN     {$$ = newlist(); listcat($$, $1); listadd($$, "."); listadd($$, $3); listadd($$, "("); listcat($$, $5); listcat($$,$6);listadd($$, ")");}
    | ID LPAREN RPAREN 
    {
        $$ = unpack($1,newlistlists());
        if(!gettype($1))
        {
            printf("// Failed to parse macrojava code.");
            exit(0);
        }
    }
    | ID LPAREN arguments RPAREN
    {
        $$ = unpack($1,$3);
        if(!gettype($1))
        {
            printf("// Failed to parse macrojava code.");
            exit(0);                     
        }
    }
    ;

exprcomma: {$$ = newlist();}
    | exprcomma COMMA expr {$$ = newlist(); listcat($$, $1); listadd($$, ","); listcat($$, $3);}
    ;

pexpr: INTLIT                           {$$ = newlist(); listadd($$, $1);}     
    | TRU                               {$$ = newlist(); listadd($$, "true");}
    | FALS                              {$$ = newlist(); listadd($$, "false");}
    | ID                                {$$ = newlist(); listadd($$, $1);}
    | THIS                              {$$ = newlist(); listadd($$, "this");}
    | NEW INT LBRACK expr RBRACK        {$$ = newlist(); listadd($$, "new"); listadd($$, "int"); listadd($$, "["); listcat($$, $4); listadd($$, "]");}
    | NEW ID LPAREN RPAREN              {$$ = newlist(); listadd($$, "new"); listadd($$, $2); listadd($$, "("); listadd($$, ")");}
    | NOT expr    %prec U_NOT           {$$ = newlist(); listadd($$, "!"); listcat($$, $2);} 
    | LPAREN expr RPAREN                {$$ = newlist(); listadd($$, "("); listcat($$, $2); listadd($$, ")");}
    ;

arguments: expr
    {
        $$ = newlistlists();
        listlistsadd($$,$1);
    } 
    | expr COMMA arguments
    {
        $$ = newlistlists();
        listlistsadd($$,$1);
        listlistscat($$,$3);
    }
    ;

%%


int main(int argc, char **argv)
{
    yyparse();
}

int yyerror(char *s)
{
    printf("// Failed to parse macrojava code.");
    return 0;
}

struct node* makenode(char* str)
{
    struct node* n = (struct node*)malloc(sizeof(struct node));
    n->str = str;
    n->next = NULL;
    return n;
}

void listadd(struct list* l, char* str)
{
    struct node* n = makenode(str);
    if(l->head == NULL)
    {
        l->head = n;
        l->tail = n;
    }
    else
    {
        l->tail->next = n;
        l->tail = n;
    }
}

void listcat(struct list* l1, struct list* l2)
{
    if(l2->head == NULL) return;
    if(l1->head == NULL)
    {
        l1->head = l2->head;
        l1->tail = l2->tail;
    }
    else
    {
        l1->tail->next = l2->head;
        l1->tail = l2->tail;
    }
}

void listprint(struct list* l)
{
    struct node* n = l->head;
    while(n != NULL)
    {
        printf("%s ", n->str);
        n = n->next;
    }
    printf("\n");
}

struct list* newlist()
{
    struct list* l = (struct list*)malloc(sizeof(struct list));
    l->head = NULL;
    l->tail = NULL;
    return l;
}

struct int_node* makeintnode(int val)
{
    struct int_node* n = (struct int_node*)malloc(sizeof(struct int_node));
    n->val = val;
    n->next = NULL;
    return n;
}

struct int_list* newintlist()
{
    struct int_list* l = (struct int_list*)malloc(sizeof(struct int_list));
    l->head = NULL;
    l->tail = NULL;
    return l;
}

void intlistadd(struct int_list* l, int val)
{
    struct int_node* n = makeintnode(val);
    if(l->head == NULL)
    {
        l->head = n;
        l->tail = n;
    }
    else
    {
        l->tail->next = n;
        l->tail = n;
    }
}

void intlistcat(struct int_list* l1, struct int_list* l2)
{
    if(l2->head == NULL) return;
    if(l1->head == NULL)
    {
        l1->head = l2->head;
        l1->tail = l2->tail;
    }
    else
    {
        l1->tail->next = l2->head;
        l1->tail = l2->tail;
    }
}

void intlistprint(struct int_list* l)
{
    struct int_node* n = l->head;
    while(n != NULL)
    {
        printf("%d ", n->val);
        n = n->next;
    }
    printf("\n");
}

struct arg_node* makeargnode(char* arg, struct int_list* positions)
{
    struct arg_node* n = (struct arg_node*)malloc(sizeof(struct arg_node));
    n->arg = arg;
    n->positions = positions;
    n->next = NULL;
    return n;
}

struct arg_pos_list* newargposlist()
{
    struct arg_pos_list* l = (struct arg_pos_list*)malloc(sizeof(struct arg_pos_list));
    l->head = NULL;
    l->tail = NULL;
    return l;
}

void argposlistadd(struct arg_pos_list* l, struct arg_node* n)
{
    if(l->head == NULL)
    {
        l->head = n;
        l->tail = n;
    }
    else
    {
        l->tail->next = n;
        l->tail = n;
    }
}

struct macrodef* makemacrodef(char* name, struct list* arguments, struct list* body, int fl)
{
    struct macrodef* m = (struct macrodef*)malloc(sizeof(struct macrodef));
    m->name = name;
    m->arguments = arguments;
    m->body = body;
    m->next = NULL;
    m->no_arguments = 0;
    m->flag_expr = fl;

    struct arg_pos_list* arg_positions = newargposlist();
    struct node* n = arguments->head;
    while(n != NULL)
    {
        struct int_list* l = newintlist();
        struct node* iter = body->head;
        int i = 0;
        while(iter != NULL)
        {
            if(strcmp(iter->str, n->str) == 0) intlistadd(l, i);
            iter = iter->next;
            i++;
        }
        struct arg_node* arg = makeargnode(n->str, l);
        argposlistadd(arg_positions, arg);
        (m->no_arguments)++;
        n = n->next;
    }
    m->arg_positions = arg_positions;

    return m;
}

void argposprint(struct arg_pos_list* l)
{
    struct arg_node* n = l->head;
    while(n != NULL)
    {
        printf("%s: ", n->arg);
        intlistprint(n->positions);
        n = n->next;
    }
    printf("\n");
}

void symboltableadd(struct symbol_table* st, struct macrodef* m)
{
    if(st->head == NULL)
    {
        st->head = m;
        st->tail = m;
    }
    else
    {
        struct macrodef* iter = st->head;
        while(iter!=NULL)
        {
            if(strcmp(iter->name, m->name) == 0)
            {
                printf("// Failed to parse macrojava code.");
                exit(0);
            }
            iter = iter->next;
        }
        st->tail->next = m;
        st->tail = m;
    }
}

struct list* unpack(char* name, struct list_lists* args)
{
    struct macrodef *iter = table.head;
    while(iter!=NULL)
    {
        if(strcmp(iter->name, name) == 0)
        {
            int sz  = 0;
            struct list* temp = args->head;
            while(temp != NULL)
            {
                sz++;
                temp = temp->next;
            }
            if(sz != iter->no_arguments)
            {
                printf("// Failed to parse macrojava code.");
                exit(0);
            }
            struct list_lists* l = newlistlists();
            struct node* n = iter->body->head;
            while(n != NULL)
            {
                listlistaddstr(l, n->str);
                n = n->next;
            }
            if(sz==0)
            {
                return collapse(l);

            }
            struct arg_node* curr_arg = iter->arg_positions->head;
            struct list* replacement = args->head;
            while(curr_arg != NULL)
            {
                struct int_node* curr_pos = curr_arg->positions->head;
                if(curr_pos == NULL)
                {
                    curr_arg = curr_arg->next;
                    replacement = replacement->next;
                    continue;
                }
                struct list* body_iter = l->head;
                int pos = curr_pos->val;
                
                int i = 0;
                while(curr_pos!=NULL)
                {
                    while(i < pos)
                    {
                        body_iter = body_iter->next;
                        i++;
                    }
                    struct list* nl = copy(replacement);
                    replace(body_iter,nl);
                    i++;
                    curr_pos = curr_pos->next;
                    body_iter = body_iter->next;
                    if(curr_pos != NULL) pos = curr_pos->val;
                }
                
                curr_arg = curr_arg->next;
                replacement = replacement->next;
            }
            return collapse(l);
        }
        iter = iter->next;
    }
    printf("// Failed to parse macrojava code.");
    exit(0);
}


struct list_lists* newlistlists()
{
    struct list_lists* l = (struct list_lists*)malloc(sizeof(struct list_lists));
    l->head = NULL;
    l->tail = NULL;
    return l;
}

void listlistsadd(struct list_lists* l, struct list* n)
{
    if(l->head == NULL)
    {
        l->head = n;
        l->tail = n;
    }
    else
    {
        l->tail->next = n;
        l->tail = n;
    }
}

void listlistsprint(struct list_lists* l)
{
    struct list* iter = l->head;
    while(iter != NULL)
    {
        listprint(iter);
        iter = iter->next;
    }
}

void listlistscat(struct list_lists* l1, struct list_lists* l2)
{
    if(l2->head == NULL) return;
    if(l1->head == NULL)
    {
        l1->head = l2->head;
        l1->tail = l2->tail;
    }
    else
    {
        l1->tail->next = l2->head;
        l1->tail = l2->tail;
    }
}

struct list * copy(struct list* l)
{
    if(l==NULL) return NULL;
    struct list* res = newlist();
    if(l->head == NULL) return res;
    struct node* n = l->head;
    while(n != NULL)
    {
        listadd(res, n->str);
        n = n->next;
    }
    return res;
}

struct list* collapse(struct list_lists* l)
{
    if(l==NULL) return NULL;
    struct list* nl = newlist();
    if(l->head == NULL) return nl;
    struct list* iter = l->head;
    while(iter!=NULL)
    {
        listcat(nl,iter);
        iter = iter->next;
    }
    return nl;
}

void listlistaddstr(struct list_lists* l, char* str)
{
    struct list* n = newlist();
    listadd(n, str);
    listlistsadd(l, n);
}

void replace(struct list* l, struct list* replacement)
{
    l->head = replacement->head;
    l->tail = replacement->tail;
}

int gettype(char* str)
{
    struct macrodef* iter = table.head;
    while(iter != NULL)
    {
        if(strcmp(iter->name, str) == 0)
        {
            return iter->flag_expr;
        }
        iter = iter->next;
    }
}