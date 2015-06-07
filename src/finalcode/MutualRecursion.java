package finalcode;

import java.util.HashMap;

//BEGIN_MUTUAL_STMT
interface StmtAlg<E, S> {
	E var(String x);
	E assign(String x, E e);
	S expr(E e);
	S comp(S e1, S e2);
}
//END_MUTUAL_STMT

//BEGIN_EVAL_STMT
interface Stmt { 
	void eval(); 
}
class StmtFactory implements StmtAlg<Exp, Stmt> {
	HashMap<String, Value> map = new HashMap<String, Value>();

	public Exp var(final String x) {
		return new Exp() {
			public Value eval() {
				return map.get(x);
			}};
	}
	public Exp assign(final String x, final Exp e) {
		return new Exp() {
			public Value eval() {
				Value v = e.eval(); 
				map.put(x, v); 
				return v;
		}};
	}
	public Stmt comp(final Stmt s1, final Stmt s2) {
		return new Stmt() {
			public void eval() {
				s1.eval(); 
				s2.eval();
		}};
	}
	public Stmt expr(final Exp e) {
		return new Stmt() {
			public void eval() {
				e.eval();
		}};
}}
//END_EVAL_STMT

class TestMutual {
	<E, S> E exp(IntAlg<E> v, StmtAlg<E, S> v2) {
		return v2.assign("x", v.add(v.lit(3), v.lit(4)));
	}
	
	<E, S> S stmt(IntAlg<E> v, StmtAlg<E, S> v2) {
		return v2.comp(v2.expr(exp(v, v2)), v2.expr(v2.var("x")));
	} 
	
	// bad
	/*
	<E, S> S stmtBad(StmtAlg<E, S> v) {
		return v.comp(exp(v), v.var("x"));
	}
	*/
	
	void test() {
		IntFactory intF = new IntFactory();
		StmtFactory factory = new StmtFactory();

		exp(intF, factory).eval();
		stmt(intF, factory).eval();
	}
} 