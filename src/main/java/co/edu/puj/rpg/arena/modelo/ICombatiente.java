package co.edu.puj.rpg.arena.modelo;

/**
 * Contrato comportamental puro (sección 1.2): define la acción defensiva mínima que cualquier
 * entidad interactiva debe ofrecer en el área de combate. No tiene estado ni implementación.
 */
public interface ICombatiente {

    /**
     * Procesa la reducción de daño según la armadura o mitigación base de la entidad.
     *
     * @param danoRecibido daño bruto que llega a la entidad antes de mitigarlo
     */
    void defender(int danoRecibido);
}
