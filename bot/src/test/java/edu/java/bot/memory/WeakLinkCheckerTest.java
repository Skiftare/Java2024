package edu.java.bot.memory;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class WeakLinkCheckerTest {

    @Test
    public void testCheckLinkWithoutConnecting_ValidWeakLink() {
        // Arrange
        String weakLink = "https://stackoverflow.com";

        // Act
        boolean result = WeakLinkChecker.checkLinkWithoutConnecting(weakLink);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void testCheckLinkWithoutConnecting_InvalidWeakLink() {
        // Arrange
        String strongLink = "https://www.google.com";

        // Act
        boolean result = WeakLinkChecker.checkLinkWithoutConnecting(strongLink);

        // Assert
        assertThat(result).isFalse();
    }

}
