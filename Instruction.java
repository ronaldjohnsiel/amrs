import java.io.*;
import java.util.*;
import java.util.regex.*;
public class Instruction{
	private String operation;
	private String op1;
	private String op2;
	private Integer val1;
	private Integer val2;

	public Instruction(String operation, String op1, String op2){
		this.operation = operation;
		this.op1 = op1;
		this.op2 = op2;
		this.val1 = 0;
		this.val2 = 0;
	}

	public void printShit(){
		System.out.println(this.operation+" "+this.op1+" "+this.op2);
	}

	public String getOperation(){
		return this.operation;
	}

	public String getOperand1(){
		return this.op1;
	}

	public String getOperand2(){
		return this.op2;
	}

	public Integer getValue1(){
		return this.val1;
	}

	public Integer getValue2(){
		return this.val2;
	}

	public void load(HashMap<String,Integer> registers) {
		if (isRegister(this.op2))
		{
			this.val2 = registers.get(this.op2);
			this.val1 = this.val2;
			registers.put(this.op1, this.val1);
		}
		else
		{
			this.val1 = this.val2;
			registers.put(this.op1, this.val1);
		}
		this.printStatus();
	}

	public void add(HashMap<String,Integer> registers) {
		if (isRegister(this.op2))
		{
			this.val1 = this.val1 + registers.get(this.op2);
			registers.put(this.op1, this.val1);
		}
		else
		{
			this.val1 = this.val1 + this.val2;
			registers.put(this.op1, this.val1);
		}
		this.printStatus();
	}

	public void sub(HashMap<String,Integer> registers) {
		if (isRegister(this.op2))
		{
			this.val1 = this.val1 - registers.get(this.op2);
			registers.put(this.op1, this.val1);
		}
		else
		{
			this.val1 = this.val1 - this.val2;
			registers.put(this.op1, this.val1);
		}
		this.printStatus();
	}

	public void cmp(HashMap<String,Integer> registers, HashMap<String,Integer> flags) {
		/*Compares the values of two registers by subtracting the value of the second register from the value of the
		first register. If the resultis zero (0),the ZF is setto 1, 0 otherwise (default). Ifthe resultis negative,the NF is set
		to 1, 0 otherwise (default). No change (default value) for NF and ZF for a positive difference.*/
		int diff = 0;
		if (isRegister(this.op1) && isRegister(this.op2))								// both are registers
			diff = registers.get(this.op1) - registers.get(this.op2);
		else if (isRegister(this.op1) && !isRegister(this.op2))					// 2nd operand is not a register
			diff = registers.get(this.op1) - this.val2;
			// sets the ZF and NF based on the difference
		if (diff == 0) flags.put("ZF", 1);
		else if (diff < 0) flags.put("NF", 1);
		// System.out.println("diff is: " + diff);
		System.out.println();
		this.printShit();
	}

	public void fetch(HashMap<String,Integer> registers)
	{
		if (isRegister(this.op1))
		{
			this.val1 = registers.get(this.op1);
		}
		else
		{
			this.val1 = Integer.parseInt(this.op1);
		}

		if (isRegister(this.op2))
		{
			this.val2 = registers.get(this.op2);
		}
		else
		{
			this.val2 = Integer.parseInt(this.op2);
		}
	}

	public void execute(HashMap<String, Integer> registers, HashMap<String, Integer> flags){
		if(this.operation.equals("add")){
			this.add(registers);
		}
		else if(this.operation.equals("sub")) {
			this.sub(registers);
		}
		else if(this.operation.equals("load")){
			this.load(registers);
		}
		else if(this.operation.equals("cmp")){
			this.cmp(registers, flags);
			System.out.println("ZF: " + flags.get("ZF") + "\n" + "NF: " + flags.get("NF"));
		}
	}

	private void printStatus() {
		//System.out.println(this.op1+" "+this.op2);
		System.out.println("\n" + this.operation + " " + this.op1 + " " + this.op2);
		System.out.println("\t" + this.op1 + ": " + this.val1 + " | " + this.op2 + ": " + this.val2);
	}

	private boolean isRegister(String operand)
	{
		Pattern REGISTERPATTERN = Pattern.compile("r[1-9]|r1[0-9]|r2[0-9]|r3[0-2]");
		Matcher m = REGISTERPATTERN.matcher(operand);
		return m.matches();
	}
}
