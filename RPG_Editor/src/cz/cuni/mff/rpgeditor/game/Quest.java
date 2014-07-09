package cz.cuni.mff.rpgeditor.game;

import java.util.ArrayList;


/**
 * Reprezentuje ukol. Kazdy ukol muze mit text pri zadavani, pri plneni a pri
 * dokonceni. Navic ma seznam prerekvizit. Pokud je seznam prazdny, hrac ma
 * moznost si nechat ukol zadat.
 */
public class Quest
{
	int id;
	ArrayList<Quest> prereq;
	String questGettingText, questSolvingText, questCompletedText;

	public Quest(ArrayList<Quest> prereq, String text)
	{
		this.prereq = prereq;
		questGettingText = text;
		questSolvingText = text;
		questCompletedText = "";
	}
}