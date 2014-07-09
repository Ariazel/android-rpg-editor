package cz.cuni.mff.rpgeditor.game;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

/**
 * Trida, ktera nacte potrebne udaje z konfiguracnich souboru pri spusteni hry.
 */
public class Loader
{
	/**
	 * Otevre soubor allies.cfg, ve kterem jsou ulozeny informace o allies a
	 * nacte je. Format souboru allies.cfg: name=STRING position-x=INT
	 * position-y=INT look=FILE_PATH quest=INT quest=INT ... ----- dalsi postavy
	 * ve stejnem formatu.
	 * 
	 * @param quests
	 *            Ukoly, ktere byly jiz drive splneny a proto neni nutne s nimi
	 *            dale pracovat.
	 * @return Seznam NPC.
	 * @throws DataFormatException
	 *             Kdyz je soubor allies.cfg ve spatnem formatu, je vyhozena
	 *             tato vyjimka.
	 * @throws FileNotFoundException
	 *             Pokud nebyl nalezen soubor allies.cfg.
	 * @throws IOException
	 *             Problem pri cteni souboru allies.cfg.
	 */
	ArrayList<Ally> loadAllies(ArrayList<Quest> quests)
			throws DataFormatException, IOException
	{
		ArrayList<Ally> allies = new ArrayList<Ally>();
		try
		{
			BufferedReader in = new BufferedReader(new FileReader("allies.cfg"));
/*			String line;
			while ((line = in.readLine()) != null)
			{
				
			}
*/
			in.close();

			return allies;

		}
		catch (FileNotFoundException e)
		{
			throw new FileNotFoundException("Unable to find file allies.cfg");
		}
		catch (IOException e)
		{
			throw new IOException("Unable to work with file allies.cfg");
		}
	} // TODO: sjednotit typ nacitani (faze vs. procedura)

	/**
	 * Otevre soubor quests.cfg, ve kterem jsou ulozeny informace o ukolech a
	 * nacte je. Format souboru quests.cfg: id=INT prereq=INT prereq=INT ...
	 * text1=STRING text2=STRING text3=STRING ----- dalsi ukoly ve stejnem
	 * formatu
	 * 
	 * @return Seznam ukolu.
	 */
	ArrayList<Quest> loadQuests() throws IOException, DataFormatException
	{
		ArrayList<Quest> quests = new ArrayList<Quest>();
		ArrayList<ArrayList<Integer>> prereqNums = new ArrayList<ArrayList<Integer>>();

		try
		{
			// TODO: opravit throws
			BufferedReader in = new BufferedReader(new FileReader("quests.cfg"));
			String line;
			Quest currentQuest = null;
			while ((line = in.readLine()) != null)
			{
				if (line.startsWith("id="))
				{
					currentQuest = new Quest(null, "");
					currentQuest.id = Integer.parseInt(line.substring(3));
					prereqNums.add(new ArrayList<Integer>());
				}
				else
					throw new DataFormatException(
							"Wrong format of file quests.cfg");


				if ((line = in.readLine()) == null)
					throw new DataFormatException(
							"Wrong format of file allies.cfg");
				while (line.startsWith("prereq="))
				{
					int questNum = Integer.parseInt(line.substring(7));
					prereqNums.get(prereqNums.size() - 1).add(questNum);
					if ((line = in.readLine()) == null)
						throw new DataFormatException(
								"Wrong format of file allies.cfg");
				}


				if (line.startsWith("text1="))
					currentQuest.questGettingText = line.substring(6);
				else
					throw new DataFormatException(
							"Wrong format of file allies.cfg");


				if ((line = in.readLine()) == null)
					throw new DataFormatException(
							"Wrong format of file allies.cfg");
				if (line.startsWith("text2="))
					currentQuest.questSolvingText = line.substring(6);
				else
					throw new DataFormatException(
							"Wrong format of file allies.cfg");


				if ((line = in.readLine()) == null)
					throw new DataFormatException(
							"Wrong format of file allies.cfg");
				if (line.startsWith("text3="))
					currentQuest.questCompletedText = line.substring(6);
				else
					throw new DataFormatException(
							"Wrong format of file allies.cfg");


				if ((line = in.readLine()) == null)
					throw new DataFormatException(
							"Wrong format of file allies.cfg");
				if (line.equals("-----"))
					quests.add(currentQuest);
				else
					throw new DataFormatException(
							"Wrong format of file allies.cfg");
				in.close();
			}

			// prevod cisel u prerekvizit na ukoly
			for (int i = 0; i < prereqNums.size(); ++i)
			{
				currentQuest = quests.get(i);
				currentQuest.prereq = new ArrayList<Quest>();
				ArrayList<Integer> cqprereqs = prereqNums.get(i);

				while (!cqprereqs.isEmpty())
				{
					// pri spatnem cisle muze vyhodit NullPointerException
					currentQuest.prereq.add(findQuestByNum(cqprereqs.get(0),
							quests));
					cqprereqs.remove(0);
				}
			}

			in.close();

			return quests;

		}
		catch (FileNotFoundException e)
		{
			throw new FileNotFoundException("Unable to find file quests.cfg");
		}
		catch (IOException e)
		{
			throw new IOException("Wrong format of file quests.cfg");
		}
	}

	private Quest findQuestByNum(int id, ArrayList<Quest> quests)
	{
		for (Quest q : quests)
		{
			if (q.id == id)
				return q;
		}

		return null;
	}

}
