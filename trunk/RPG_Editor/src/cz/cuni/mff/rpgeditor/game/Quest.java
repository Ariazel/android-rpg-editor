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
	String quest_getting_text, quest_solving_text, quest_completed_text;
	boolean solved;

	public Quest(ArrayList<Quest> prereq, String text)
	{
		this.prereq = prereq;
		quest_getting_text = text;
		quest_solving_text = text;
		quest_completed_text = "";
	}
}