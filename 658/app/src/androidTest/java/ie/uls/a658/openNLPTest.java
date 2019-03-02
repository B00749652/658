package ie.uls.a658;

import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ie.uls.TextChk;

import static junit.framework.TestCase.assertTrue;


@RunWith(AndroidJUnit4.class)
public class openNLPTest {
   private static   String rawinput = "The classic science fiction novel that captures and expands on the vision of Stanley Kubrick´s" +
        "immortal film-and changed the way we look at the stars and ourselves. From the savannas of Africa at the" +
        "dawn of mankind to the rings of Saturn as man ventures to the outer rim of our solar system, 2001: A" +
        "Space Odyssey is a journey unlike any other. This allegory about humanity´s exploration of the universe-and" +
        "the universe`s reaction to humanity-is a hallmark achievement in storytelling that follows the crew of the" +
        "spacecraft Discovery as they embark on a mission to Saturn. Their vessel is controlled by HAL 9000, an" +
        "artificially intelligent supercomputer capable of the highest level of cognitive functioning that" +
        "rivals-and perhaps threatens-the human mind. Grappling with space exploration, the perils of technology," +
        "and the limits of human power, 2001: A Space Odyssey continues to be an enduring classic of cinematic" +
        "scope.";

   @Test
   public void init(){
       Resources res = InstrumentationRegistry.getTargetContext().getResources();
       TextChk scan = new TextChk(rawinput);
       Map<String,List<String>> noun = new HashMap<>();
       noun.put("noun1",scan.getNouns());
       List<String> result = noun.get("noun1");
       assertTrue(100 > result.size());

   }


}

