package ru.apricom.testapp.entities.auth;

/**
 * @author leonid.
 *
 * List of roles in the system
 */
public enum RolesNames {
    SYSTEM_ADMIN,
    CAMPAIGN_ADMIN,  /* администратор приемной компании */
    TECH_SECRETARY, /* техсек */
    HEADMASTER,  /* столоначальник */
    CHIEF_HEADMASTER, /* старший столоначальник */
    BOARD_MANAGER,    /* менеджер */
    BOARD_INSPECTOR,  /* проверка */
    EXECUTIVE_SECRETARY, /* ответственный секретарь */

    ENTRANT  /* абитуриент */

}
