/*DEBUG
NO FUNCIONA LA LÓGICA DEL PROGRAMA ... SE QUEDA EN BUCLE Y NO DA EL RESULTADO...

HACER:
COMPROBAR:
    Condiciones de salida del bucle: Asegúrate de que tus bucles tienen condiciones de salida adecuadas para evitar que se queden en un bucle infinito.

    Lógica de procesamiento: Revisa la lógica de procesamiento en tu programa para asegurarte de que esté realizando las operaciones correctas en el orden adecuado.

    Manejo de errores: Asegúrate de que tu programa esté manejando correctamente los errores y excepciones que puedan ocurrir durante la ejecución.

    Depuración: Utiliza herramientas de depuración para examinar el estado de tu programa mientras se ejecuta. Esto puede ayudarte a identificar dónde se está produciendo el problema.

    Pruebas por partes: Divide tu programa en partes más pequeñas y pruébalas por separado para identificar dónde se encuentra el problema.

*/
//Debug: try reference_param instead value_param: p, ...
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Calculadora {

    static final int MAX_CADENA = 256;
    static final int MAX_LONGITUD = 81;
    static final double PC = 3.086 * Math.pow(10, 16);
    static int formula_len=0; //almacenará la longitud evaluable de formula
    static char[] formula = new char[MAX_CADENA];//new char[MAX_LONGITUD];
    static double resultado = 0;
    static boolean error = false;
    static boolean fact = false;
    static int ruptura; // Declaración de la variable 'ruptura'
    static int ptr; //puntero externo para recorrer formula
    static int p; //puntero de eval()
    static int i;
    static double f;
    static char c;
    //String input;
    public static void main(String[] args) {
        char[] aux_formula = new char[MAX_CADENA];     
	
        //resultado = 0;
        error = false;
        int contador;
        
        String input="";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            input = reader.readLine(); // Leer la cadena
            System.out.println("Entrada: " + input);
        } catch (IOException e) {
            System.err.println("Error al leer la entrada");
        }        
        /*
        aux_formula = input.toCharArray();
        for(contador=0;contador < aux_formula_len;contador++) {
        	formula[contador]=aux_formula[contador];
        }
        formula[contador] = '\\';
        */
        //++contador;
        //formula[contador] = '\n';
        //System.out.println("formula.lenght= " + formula_len);
        //System.out.println("formula=" + formula.toString());
        //formula = input.toCharArray();
        System.out.println("input="+ input);
        System.out.println("input.length()=" +input.length());
        aux_formula = input.toCharArray();
        for(contador=0;contador < input.length();contador++) {
        	formula[contador] = aux_formula[contador];
        }
        formula_len=contador;
        formula[contador] = '}'; //indica fin de la fórmula
        System.out.println("aux_formula=" + copy(aux_formula,0,aux_formula.length) );//aux_formula.toString());
        System.out.println("formula=" + formula.toString());
        System.out.println("formula_len= " + formula_len);
        System.out.println("Volcado formula:");
        for(char car:formula) {
        	System.out.print(car);
        }
        System.out.println("Fin volcado ^ ");
        resultado = calcularFormula();
        if (error) {
            System.out.println("\nError!\n");
            System.out.println(input);
            for (int i = 0; i < p; i++)
                System.out.print(" ");
            System.out.println("^__");
        } else
            System.out.printf("\n%.19f\n", resultado);
    }

    static char[] ch2ptr(char ch) {
        char[] cp = new char[1];
        cp[0] = ch;
        return cp;
    }

    static String copy(char[] cadena, int index, int count) {
        char[] cp = new char[count];
        String aux="";
        System.out.println("copy: index=" + index + " count=" + count);
        for (int i = index, j = 0; j < count; i++, j++) {
            cp[j] = cadena[i];
        }
        aux+=String.copyValueOf(cp);
        return aux;
    }

    static double calcularFormula() {
        double r;
        System.out.println("formula (calcularFormula=" + copy(formula,0,formula_len));
        //int p = 0; // Inicializamos 'p' en 0
        p = 0; //p variable global
        eval();
        r = resultado;
        return r;
    }

    static void eval() {
        //static int p;
        //static int i;
        int l;
        //char c;
        char[] aux = new char[MAX_CADENA];
        l = formula_len;
        for ( i = 0; i < l; i++)
            formula[i] = Character.toUpperCase(formula[i]);
        
        if (formula[0] == '.') {
        	System.out.println("condición . !!!!");
            //aux = new char[formula_len + 1];
        	//formula->{  '.','1','}'  }
        	// i     ->     0  1   2  
        	//aux    ->{  '0','.','1','2','}' }
        	// i     ->{   1   2   3   4   5 
            aux[0] = '0';
            aux[1] = '.';
            for ( i = 2; i <= formula_len+2; i++)
                aux[i] = formula[i-1];
            //formula = aux;
            for ( i=0;i<=formula_len+1;i++) {
            	formula[i]=aux[i];
            	//formula_len+=1;
            }
            formula_len+=1; //incrementar la longitud evaluable de la variable formula
            System.out.println("formula (.x except!!!)=" + copy(formula,0,formula_len));

        }
        l = formula_len;
        if (formula[0] == '+') {
            for ( i = 0; i < l; i++)
                formula[i] = formula[i + 1];
            formula[l - 1] = '}';
        }
        p = -1;
        c = sigp();
        resultado = expr();
        //if (c == '}')
        if(formula[p] == '}')
            error = false;
        else {
        	//debug
        	System.out.println("CONDICION DE ERROR:");
        	System.out.println("c=" + c);
        	System.out.println("p=" + p);
        	System.out.println("formula[p]=" + formula[p]);
        	
            error = true;
        }
        ruptura = p;
        //return error;
    }

    static char sigp() {
        char c;
        do {
            p++;
            System.out.println("p="+p);
            if (p < formula_len) { //recalcular la longitud en función de formula[k]=='}'
                c = formula[p];
            //debug
            	System.out.println("c=" + c);
            }
            else {
                c = '\\';
                System.out.println("c=" + c);
            }
        } while (c == ' ');
        return c;
    }
    
    static double expr() {
        double e;
        char operador;
        e = exprsimp();
        while ((c == '+') || (c == '-')) {
            operador = c;
            c = sigp();
            switch (operador) {
                case '+':
                    e = e + exprsimp();
                    break;
                case '-':
                    e = e - exprsimp();
                    break;
            }
        }
        return e;
    }
    static double exprsimp() {
        double s;
        char operador;
        s = termino();
        while ((c == '*') || (c == '/')) {
            operador = c;
            c = sigp();
            switch (operador) {
                case '*':
                    s = s * termino();
                    break;
                case '/':
                    s = s / termino();
                    break;
            }
        }
        return s;
    }

    static double termino() {
        double t;
        t = fact_s();
        while (c == '^') {
            c = sigp();
            t = Math.pow(t, fact_s());
        }
        return t;
    }

    static double fact_s( ) {
        //double f;
        if (c == '-') {
            c = sigp();
            if (fact) {
                p--;
                ruptura = p;
                return -1;
            }
            return -fct();
        }
        return fct();
    }

    static double fct() {
        double f;
        if (Character.isDigit(c) || c == '.')
            f = proc_as_num();
        else if (c == '(')
            f = proc_as_new_exp();
        else if (c == '-')
            f = fact_s();
        else
            f = proc_like_stdfunc();
        return f;
    }

    static double proc_as_num() {
        int inicio;
        //boolean error = false;
        
        char ch;
        inicio = p;
        do {
            if ((c == '.') && fact)
                return -1;
            c = sigp();
        } while (Character.isDigit(c));
        if (c == '.') {
            if (fact)
                error = true;
            int l = p;
            do {
                c = sigp();
            } while (Character.isDigit(c));
            System.out.println("formula="+formula.toString());
            //f = Double.parseDouble(copy(formula, inicio, p - inicio).toString());
            f=Double.parseDouble(String.copyValueOf(formula, inicio, p-inicio));
            //debug
            System.out.println(f);
            
            if ((f != 0) && error) {
                p = l;
                return f;
            }
        }
        if (c == 'E') {
            ch = sigp();
            int l = p;
            do {
                c = sigp();
            } while (Character.isDigit(c));
            if ((ch == '-') && (++l == p) && (c == '}')) {
                ruptura = --p;
                c = ch;
            }
        }
        if (c == '.') {
            ruptura = p;
            return f;
        }
        //System.out.println("formula="+formula.toString());
        
        //f = Double.parseDouble(copy(formula, inicio, p - inicio).toString());
        f=Double.parseDouble(String.copyValueOf(formula, inicio, p-inicio));
        //debug
        System.out.println(f);
        if (c == '!') {
            f = fact(f);
            c = sigp();
        } else if (c == '²') {
            f = f * f;
            c = sigp();
        }
        return f;
    }

    static double proc_as_new_exp() {
        double f;
        c = sigp();
        f = expr();
        if (c == ')') {
            c = sigp();
            if (c == '²') {
                f *= f;
                c = sigp();
            } else if (c == '!') {
                f = fact(f);
                c = sigp();
            }
        } else
            ruptura = p;
        return f;
    }

    static double fact(double f) {
        if (f > 0)
            return f * fact(f - 1);
        return 1;
    }

    static double proc_like_stdfunc() {
        double f = 0;
        System.out.println("evaluando proc_like_stdfunc");
        if (copy(formula, p, 3).equals("ABS")) {
            p += 2;
            c = sigp();
            f = fct();
            f = Math.abs(f);
        } else if (copy(formula, p, 4).equals("SQRT")) {
            p += 3;
            c = sigp();
            f = fct();
            f = Math.sqrt(f);
        } else if (formula[p] == '¿') {
            c = sigp();
            f = fct();
            f = Math.sqrt(f);
        } else if (copy(formula, p, 3).equals("SQR")) {
            p += 2;
            c = sigp();
            f = fct();
            f *= f;
        } else if ( copy(formula, p, 3).equals("SIN")) {
        	System.out.println("MATCH SIN");
            p += 2;
            c = sigp();
            f = fct();
            f = Math.sin(f);
        } else if (copy(formula, p, 3).equals("SEN")) {
            p += 2;
            c = sigp();
            f = fct();
            f = Math.sin(f);
        } else if (copy(formula, p, 3).equals("COS")) {
            p += 2;
            c = sigp();
            f = fct();
            f = Math.cos(f);
        } else if (copy(formula, p, 3).equals("TAN")) {
            p += 2;
            c = sigp();
            f = fct();
            f = Math.tan(f);
        } else if (copy(formula, p, 2).equals("TG")) {
            p += 1;
            c = sigp();
            f = fct();
            f = Math.tan(f);
        } else if (copy(formula, p, 6).equals("ARCTAN")) {
            p += 5;
            c = sigp();
            f = fct();
            f = Math.atan(f);
        } else if (copy(formula, p, 2).equals("LN")) {
            p += 1;
            c = sigp();
            f = fct();
            f = Math.log(f);
        } else if (copy(formula, p, 3).equals("LOG")) {
            p += 2;
            c = sigp();
            f = fct();
            f = Math.log10(f);
        } else if (copy(formula, p, 2).equals("PI")) {
            p += 1;
            c = sigp();
            f = Math.PI;
        } else if (copy(formula, p, 2).equals("UA")) {
            p += 1;
            c = sigp();
            f = 149597870700L;
        } else if (copy(formula, p, 2).equals("PC")) {
            p += 1;
            c = sigp();
            f = PC;
        } else if (formula[p] == 'C') {
            c = sigp();
            f = 299792458L;
        } else if (copy(formula, p, 3).equals("EXP")) {
            p += 2;
            c = sigp();
            f = fct();
            f = Math.exp(f);
        } else if (formula[p] == 'E') {
            //p += 1;
        	System.out.println("MATCH e");
            c = sigp();
            f = Math.E;
        } else if (copy(formula, p, 4).equals("FACT")) {
            p += 3;
            c = sigp();
            fact = true;
            f = fct();
            fact = false;
            if (f < 0) {
                return f;
            }
            f = fact(f);
        }
        ruptura = p;
        return f;
    }
}
