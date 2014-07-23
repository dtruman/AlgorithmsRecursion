import java.util.ArrayList;
import java.util.List;

import edu.neumont.nlp.DecodingDictionary;


public class ExhaustiveDecoder
{
	DecodingDictionary dd=new DecodingDictionary();
	
	//********DashDotDotDotDash Lab
	public List<String> decode(String message)
	{
		System.out.println("Start");

		ArrayList<MorseCodeInfo> possibilities=new ArrayList<MorseCodeInfo>();
		for(int i=0; i<message.length(); i++)
		{	
			if(!(dd.getWordsForCode(message.substring(0,i))==null))
			{
				for(String var : dd.getWordsForCode(message.substring(0,i)))
				{
					if(!var.contains(".") && !var.contains("/") && !var.contains("-"))
					{
						possibilities.add(new MorseCodeInfo(var, i, 1));
					}
				}
			}
		}
		
		ArrayList<MorseCodeInfo> answers=new ArrayList<MorseCodeInfo>();
		
		decodeRecursion(message, possibilities, answers);
		
		
		//This part is to just get rid of extra possibilities and sort them to only get the best possibilities
		top20(answers, 0, answers.size()-1);
		
		List<String> ret=new ArrayList<String>();
		
		for(int i=0; i<answers.size() && i<25; i++)
		{
			ret.add(answers.get(i).word + " " + (i+1));
		}
		
		return ret;
	}
	
	public void decodeRecursion(String message, ArrayList<MorseCodeInfo> al, ArrayList<MorseCodeInfo> answer)
	{
		for(int i=0; i<al.size(); i++)
		{
			MorseCodeInfo soFar=al.get(i);
			helperDecode(al.get(i), message.substring(al.get(i).length), soFar, message, answer);
		}
	}
	//Helper function
	public void helperDecode(MorseCodeInfo before, String restOf, MorseCodeInfo soFar, String message, ArrayList<MorseCodeInfo> answer)
	{
		for(int i=restOf.length(); i>=0; i--)	//Runs through the rest of the morse code string (whatever is left after the last call
		{
			if(!(futureWordCheck(restOf, i)))	//Check that there are words found in the selection of morse code
			{
				for(String var : dd.getWordsForCode(restOf.substring(0,i)))		//Gets those words
				{
					if(!var.contains(".") && !var.contains("/") && !var.contains("-"))		//Gets rid of any garbage
					{
						float a=dd.frequencyOfFollowingWord(before.word, var);		//Finds the frequency of this word compared to last word

						double b=(a/10000);

						if(a>=170 && b>0.008)		//Check if the word meets the frequency threshold
						{
							MorseCodeInfo mi=new MorseCodeInfo(var, i, a);		//Create a new info of morse code to pass in as a parameter of the recursive function as the before string
							
							MorseCodeInfo newSoFar=new MorseCodeInfo(soFar.word+var, soFar.length+i, soFar.score+a);		//Create a new info of morse code to pass in as a paramter of the recursive function as the soFar string
							
							helperDecode(mi, restOf.substring(i), newSoFar, message, answer);		//Calls the function again
						}
					}
				}
			}
		}
		if(soFar.length==message.length())
		{
			answer.add(soFar);		//If the function doesn't have any more string to look at it will check if the message sofar meets the requirements and adds it to a list of answers
		}
		
		return;
	}
	
	public boolean futureWordCheck(String restOf, int i)
	{
		return (dd.getWordsForCode(restOf.substring(0,i))==null);
	}
	
	//***************DashDotDotDotDash Lab
	
	
	
	
	
	
	//****Gets rid of extra strings and sorts the possibilities
	public void top20(ArrayList<MorseCodeInfo> al, int left, int right)
	{
		int index = partition(al, left, right);
	      if (left < index - 1)
	            top20(al, left, index - 1);
	      if (index < right)
	            top20(al, index, right);
	}
	
	public int partition(ArrayList<MorseCodeInfo> al, int left, int right)
	{
		int i=left, j=right;
		MorseCodeInfo tmp;
		float pivot=al.get((left+right)/2).score;
		
		while(i<=j)
		{
			while(al.get(i).score<pivot)
				i++;
			while(al.get(j).score>pivot)
				j--;
			if(i<=j)
			{
				tmp=al.get(i);
				al.set(i, al.get(j));
				al.set(j, tmp);
				i++;
				j--;
			}
		}
		
		return i;
	}
}