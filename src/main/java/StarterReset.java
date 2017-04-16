import java.io.File;


public class StarterReset {
	
	 private static File starterReset = new File("Data/starters/FirstStarterRecieved");

     public static void starterReset(String[] args)
     {
             if(starterReset.exists() && starterReset.isDirectory())
             {
                     File[] startersReset = starterReset.listFiles();
             for(int i = 0; i < startersReset.length; i++)
             {
                     resetEcoChar(startersReset[i]);
                     System.out.println("Wiping starter kit ip's for: "+startersReset[i].toString());
                     }
             }
          
     }

	private static void resetEcoChar(File file) {
		// TODO Auto-generated method stub
		
	}

}
