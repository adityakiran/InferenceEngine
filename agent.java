import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
class agent
{
	public static void main(String []a)
	{
	//	System.out.println("aditya");
		BufferedReader br=null;
		BufferedWriter br1 = null;
		String goal="";
		String []input;
		int inputnum;
		try {
	         br = new BufferedReader(new FileReader("input.txt"));
		     goal = goal+br.readLine();
		     inputnum = Integer.parseInt(br.readLine());
		     input = new String[inputnum];
		     int i=0;
		     while(i<inputnum)
		     {
		    	 input[i] = br.readLine();
		    	 i++;
		     }
		  //   System.out.println(input[0]);
		     ArrayList<String> constants = new ArrayList<String>();
		     HashMap<String,ArrayList<ArrayList<ArrayList<String>>>> map1 = Seperate(input,constants,goal);
		   //  System.out.println(map1);
		    // System.out.println(map1.get("Diagnosis"));
		   //  String binding="";
		     br1 = new BufferedWriter(new FileWriter("output.txt"));
		     boolean ans = FindAnswer(goal,map1);
		     if(ans)
		    	br1.write("TRUE"); 
		     else
		    	br1.write("FALSE");
		   br.close();
		   br1.close();
		         
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		//System.out.println(goal);
		
	}
	public static boolean FindAnswer(String goal,HashMap<String,ArrayList<ArrayList<ArrayList<String>>>> map)
	{
		//complete the ideal scenario
		//System.out.println("aditya");
		String key="";
		String []args = new String[2];
		//Constant []cargs = new Constant[2];
		int start=0,end=0;
		for(int i=0;i<goal.length();i++)
		{
			if(goal.charAt(i)=='(')
			{
				key = key+goal.substring(0,i);
			//	Constant ckey = new Constant(key);
				start=i+1;
			}
			if(goal.charAt(i)==')')
				end = i;
		}
		if(goal.substring(start,end).contains(","))
			{
			args[0] = goal.substring(start,end).split(",")[0];
			args[1] = goal.substring(start,end).split(",")[1];
			
			}
		else
		   {
			args[0]= goal.substring(start,end);
			args[1]="";
		//	cargs[0] = new Constant(args[0]);
		//	cargs[1] = new Constant(args[1]);
		   }
		//check if there is a fact directly first..then we can do backward tracking
		  if(DirectCheck(map,args,key))
			  return true;
		//the real game starts.I need to check in case backward chaining can be done.First 
		//I need to perform unification
		for(int i=0;map.containsKey(key)&&i<map.get(key).size();i++)
		{
			if(args[1].equals(""))
			{
			 if(map.get(key).get(i).get(0).size()==2)
				 continue;
			}
			else
			{
				if(map.get(key).get(i).get(0).size()==1)
					 continue;
			}
			if(map.get(key).get(i).get(1).size()==0)
				continue;
			//unification start
			boolean res = Unify(args,map.get(key).get(i).get(0));
			 if(res)
			 {
				 
					 //process all the predicates
					//split the predicate
					boolean answer =true;
					for(int g=0;g<map.get(key).get(i).get(1).size();g++) 
					{
					//apply the value of x to the predicates also
					//System.out.println(key+"\t"+map.get(key).get(i).get(1).get(g));	
					answer = FindAnswer(map.get(key).get(i).get(1).get(g),map);
					if(!answer)
						break;
					}
					if(answer)
						return true;
					
			 }
		}
		return false;
	}
	public static boolean Unify(String []args,ArrayList<String> arg)
	{
		//args always have constant value
		
		//String []res = new String[2];
		//res[0] = binding;
		boolean res = true;
		
			for(int i=0;i<arg.size();i++)
			{
			 if((!arg.get(i).equals(""))&&Character.isUpperCase(arg.get(i).charAt(0)))
			 {
				// System.out.println("jjjjj"+arg.get(i)+"jajaja"+args[i]);
				 //both are constants..no functions
				 if(!args[i].equals(arg.get(i)))
				 {   
					 res = false;
					 return res;
				 }
			 }
			 }
		
		
		return res;
	}
	public static boolean DirectCheck(HashMap<String,ArrayList<ArrayList<ArrayList<String>>>> map,String []args,String key)
	{
		for(int i=0;map.containsKey(key)&&i<map.get(key).size();i++)
		{
			if(map.get(key).get(i).get(1).size()!=0)
				continue;
			if(args[1]=="")
			{
				if(map.get(key).get(i).get(0).size()==2)
					continue;
				if((map.get(key).get(i).get(0).get(0).equals(args[0])))
				{
					//System.out.println("HJHJ1");
					return true;
				}
			}
			//System.out.println(map.get(key).get(i).get(0).get(0)+""+map.get(key).get(i).get(0).get(1));
			if((map.get(key).get(i).get(0).get(0).equals(args[0])&&map.get(key).get(i).get(0).get(1).equals(args[1])))
			{
				//System.out.println("HJHJ2");
				return true;
			}
		}
		//System.out.println("HJHJ3");
		return false;
	}
	
	public static HashMap<String,ArrayList<ArrayList<ArrayList<String>>>> Seperate(String[] input,ArrayList<String> constants,String goal)
	{
		// = new ArrayList<String>();
	//store prefixes in a prefix arraylist
		ArrayList<String> prefixes = new ArrayList<String>();
		HashMap<String,ArrayList<ArrayList<ArrayList<String>>>> map = new HashMap<String,ArrayList<ArrayList<ArrayList<String>>>>();
		//store the constants of the goal also
		int inert=0;
		for(int j=0;j<goal.length();j++)
		{
			if(goal.charAt(j)=='(')
			{
				inert = j;
			    break;
			}	
		}
		String inertarg =goal.substring(inert+1,goal.length()-1);
		if(inertarg.contains(","))
		{
			for(int h=0;h<inertarg.split(",").length;h++)
			{
				if(Character.isUpperCase(inertarg.split(",")[h].charAt(0)))
				     if(!constants.contains(inertarg.split(",")[h]))
				    	 constants.add(inertarg.split(",")[h]);
			}
		}
		else
		  constants.add(inertarg);
		
		for(int i=0;i<input.length;i++)
		{
			
		if(input[i].contains("=>"))
		{
			
			//Split the sentence
			String[] split1 = input[i].split("=>");
			String key = split1[1];
			ArrayList<String> tmp1 = new ArrayList<String>();
			ArrayList<String> tmp2 = new ArrayList<String>();
			ArrayList<ArrayList<String>> tmp3 = new ArrayList<ArrayList<String>>();
			//storing tmp1
			//String key1 = [i];
			int start=0;
			for(int j=0;j<key.length();j++)
			{
				if(key.charAt(j)=='(')
				{
				  start=j;
				  break;
				}
			}
			if(!prefixes.contains(key.substring(0,start)))
			prefixes.add(key.substring(0,start));
			String args = key.substring(start+1,key.length()-1);
			String mainkey = key.substring(0,start);
			ArrayList<ArrayList<ArrayList<String>>> tmp=new ArrayList<ArrayList<ArrayList<String>>>();
			if(map.containsKey(key.substring(0,start)))
			{
				//Take a temp key variable called key1
				
				tmp = map.get(key.substring(0,start));
                
				// = new ArrayList<ArrayList<String>>();
				
			}
				start=0;//korlepa
				//split using ','
				 if(args.contains(","))
				 {
					 
					 for(int m=0;m<args.split(",").length;m++)
					 {
						 
						// System.out.println("lslls");
						 if(Character.isUpperCase(args.split(",")[m].charAt(0)))
						 {
						     if(!constants.contains(args.split(",")[m]))
							 constants.add(args.split(",")[m]);
						   
						 }
					      tmp1.add(args.split(",")[m]);
					 }
				 
				 }
				 else
				 {
					 
					 if(Character.isUpperCase(args.charAt(0)))
					 {
						 if(!constants.contains(args))   
						 constants.add(args);
					 }
				      tmp1.add(args);
				 }
				//Store the arguments
				 tmp3.add(tmp1);
			
			//Split the predicate
			 if(split1[0].contains("&"))
			  {
			  // System.out.println(split1[0].split("&")[0]);
			  for(int k=0;k<split1[0].split("&").length;k++)
			  {
				  tmp2.add(split1[0].split("&")[k]);
			    //find out the constant in each predicate
				  
				  for(int j=0;j<split1[0].split("&")[k].length();j++)
					{
						if(split1[0].split("&")[k].charAt(j)=='(')
						{
						  start=j;
						  break;
						}
					}
				  if(!prefixes.contains(split1[0].split("&")[k].substring(0,start)))
				  prefixes.add(split1[0].split("&")[k].substring(0,start));
				//  System.out.println("JJJJ"+split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1));
				  if(split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1).contains(","))
				  {
					  for(int m=0;m<split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1).split(",").length;m++)
						 {
						    
							 if(Character.isUpperCase(split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1).split(",")[m].charAt(0)))
							 {
								 if(!constants.contains(split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1).split(",")[m]))
								 constants.add(split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1).split(",")[m]);
							 }
								 //tmp1.add(args.split(",")[m]);
						 }
				  }
				  else
				  {
					  if(Character.isUpperCase(split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1).charAt(0)))
					  {  
						if(!constants.contains(split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1)))
						constants.add(split1[0].split("&")[k].substring(start+1,split1[0].split("&")[k].length()-1));  
					  }
					  }
			  }
			  }
			else
			{
			  tmp2.add(split1[0]);
			  for(int g=0;g<split1[0].length();g++)
			  {
				  if(split1[0].charAt(g)=='(')
			         start=g;
			  }	
			  if(!prefixes.contains(split1[0].substring(0,start)))
			  prefixes.add(split1[0].substring(0,start));
			  if(split1[0].substring(start+1,split1[0].length()-1).contains(","))
				 {
					 for(int m=0;m<split1[0].substring(start+1,split1[0].length()-1).split(",").length;m++)
					 {
						 if(Character.isUpperCase(split1[0].substring(start+1,split1[0].length()-1).split(",")[m].charAt(0)))
						 {  
							 if(!constants.contains(split1[0].substring(start+1,split1[0].length()-1).split(",")[m]))
							 constants.add(split1[0].substring(start+1,split1[0].length()-1).split(",")[m]);
						 }
						  //tmp1.add(split1[0].split(",")[m]);
					 }
				 }
				 else
				 {
					 if(Character.isUpperCase(split1[0].substring(start+1,split1[0].length()-1).charAt(0)))
					 {
						 if(!constants.contains(split1[0].substring(start+1,split1[0].length()-1)))
						 constants.add(split1[0].substring(start+1,split1[0].length()-1));
					 }
				 }
			}
			 tmp3.add(tmp2);
			 tmp.add(tmp3);
			// System.out.println(zmp);
			 map.put(mainkey,tmp);
			}		
		else
		{
			//how to store rules with only facts
			String key = input[i];
			int start=0;
			for(int j=0;j<key.length();j++)
			{
				if(key.charAt(j)=='(')
				{
				  start=j;
				  break;
				}
			}
			String mainkey = key.substring(0,start);
			if(!prefixes.contains(key.substring(0,start)))
			prefixes.add(key.substring(0,start));
			String args = key.substring(start+1,key.length()-1);
			ArrayList<ArrayList<ArrayList<String>>> tmp=new ArrayList<ArrayList<ArrayList<String>>>();
			if(map.containsKey(key.substring(0,start)))
			{
				//Take a temp key variable called key1
                tmp = map.get(key.substring(0,start));
				// = new ArrayList<ArrayList<String>>();
			}
				ArrayList<String> tmp1 = new ArrayList<String>();
				ArrayList<String> tmp2 = new ArrayList<String>();
				ArrayList<ArrayList<String>> tmp3 = new ArrayList<ArrayList<String>>();
				//split using '
				 if(args.contains(","))
				 {
					 for(int m=0;m<args.split(",").length;m++)
					 {
						 tmp1.add(args.split(",")[m]);
						 if(Character.isUpperCase(args.split(",")[m].charAt(0)))
						 {
							 if(!constants.contains(args.split(",")[m]))
							 constants.add(args.split(",")[m]);
						 }
					 }
				 }
				 else
				 {
					 tmp1.add(args);
				     if(Character.isUpperCase(args.charAt(0)))
				     {
				    	 if(!constants.contains(args))
				    	 constants.add(args);
				     }
				 }
				//Store the arguments
				 tmp3.add(tmp1);
				 tmp3.add(tmp2);
				 tmp.add(tmp3);
			map.put(mainkey,tmp);
			
		}
		//System.out.println(map.size());
		}
		
		//System.out.println(map.get("Diagnosis"));
		//printing the constants
		//for(int h=0;h<constants.size();h++)
			//System.out.println(constants.get(h));
		//with the constants I would create such a map replace
		//all x values with all the constants
		
		for(int h=0;h<prefixes.size();h++)
		{
			String key = prefixes.get(h);
			int gl=0;
			
			if(map.containsKey(key))
			{
				
				gl=1;
				//replicate in the map for all x values.
				if(map.get(key).size()>0)
				{
					//System.out.println("key"+key+"\t"+map.get(key));
					int sz = map.get(key).size();
					for(int g=0;g<sz;g++)
					{
					
					ArrayList<ArrayList<ArrayList<String>>> tmp=map.get(key);
					tmp.get(g).get(0);
					tmp.get(g).get(1);
					
				    
					for(int k=0;k<constants.size();k++)
					{
						ArrayList<String> tmp4 = new ArrayList<String>();
						ArrayList<String> tmp5 = new ArrayList<String>();
						tmp4 = (ArrayList<String>) tmp.get(g).get(0).clone();
				        tmp5 = (ArrayList<String>) tmp.get(g).get(1).clone();
				        ArrayList<ArrayList<String>> tmp6 = new ArrayList<ArrayList<String>>();
					  //iterate through tmp4
				       
						for(int l=0;l<tmp4.size();l++)
						{
							if(tmp4.get(l).equals("x"))
							{
								gl=2;
								tmp4.set(l,constants.get(k));
							}
						}
					 // iterate through tmp5
						for(int l=0;l<tmp5.size();l++)
						{
							int n=0,flag=0;
						  //  System.out.println("n\t"+tmp5.get(l));
						   for(n=0;n<tmp5.get(l).length();n++)
						   {
							//   System.out.println("n\t"+tmp5.get(l).charAt(n));
							   if(tmp5.get(l).charAt(n)=='x'&&tmp5.get(l).charAt(n-1)=='('||tmp5.get(l).charAt(n)=='x'&&tmp5.get(l).charAt(n-1)==','&&tmp5.get(l).charAt(n+1)==')')
								   {
								   gl=2;
								   flag=1;
								   break;
								   }
						   }
						  if(flag==1)
						  tmp5.set(l,tmp5.get(l).substring(0,n)+constants.get(k)+tmp5.get(l).substring(n+1,tmp5.get(l).length()));
						}
					   tmp6.add(tmp4);
					   tmp6.add(tmp5);
					   tmp.add(tmp6);
					                       //removes the first element
					   map.put(key,tmp);
					//   System.out.println("map\t"+map);
					}
				//	System.out.println("Array\t"+tmp.get(g).get(0));
					}
					if(gl==2)
						map.get(key).remove(0);
				}
			}
			
		}

		/*for(int h=0;h<prefixes.size();h++)
			map.get(h).remove(0);*/
		return map;	
	}
}

