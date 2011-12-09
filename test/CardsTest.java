import models.Card;
import models.Folder;
import models.Rating;
import models.User;

import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class CardsTest extends UnitTest {

    @Test
    public void testSchedule() {
    	Fixtures.deleteAll();
    	User user = User.create("max@mzumazumx.de", "password");
    	assertTrue(Card.due(user).isEmpty());
    	Folder f1 = Folder.create(user, "testFolder");
    	assertTrue(Card.due(f1).isEmpty());
    	Card c1 = Card.create(f1, "front", "back");
    	assertFalse(Card.due(user).isEmpty());
    	assertFalse(Card.due(f1).isEmpty());
    	assertTrue(Card.due(user).contains(c1));
    	assertTrue(Card.due(f1).contains(c1));
    	c1.schedule(Rating.WRONG);
    	assertTrue(Card.due(user).contains(c1));
    	c1.schedule(Rating.EASY);
    	assertFalse(Card.due(user).contains(c1));
    }

}
