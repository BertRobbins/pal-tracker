package org.springframework.boot.actuate.health;

public interface HealthIndicator {
    /**
     * Return an indication of health.
     * @return the health for
     */
    Health health();
}
