package com.copyright.ccc.business.services.user;

/**
 * Provides a set of constants used by the services package.
 */
public final class UserServicesConstants
{
    /**
     * Contains message strings.
     */
    public static final class Messages
    {
        public static final String IMPOSSIBLE_MOD_STATE = "Impossible ModState: {0}";

        public static final String SHOULD_NOT_BE_CALLED_ON_DIRTY_OBJECTS =
            "This method should not be called on DIRTY objects: {0}";

        public static final String CUSTOMER_DOES_NOT_HAVE_ORG_ATTRIBUTE =
            "Customer does not have an organizational attribute: {0}";

        public static final String AGREEMENT_NOT_IN_SUITABLE_STATE_FOR_SAVE =
            "The Agreement is not in a state suitable for save";

        public static final String USER_HAS_MULTIPLE_ORG_ATTRIBUTE_VALUES =
            "The current CCUser ID {0} has multiple OrgAttributeValues: {1}";

        public static final String USER_ORG_ATTRIBUTE_VALUE_SHOULD_NEVER_BE_DIRTY =
            "The CCUser OrgAttributeValue should never be DIRTY";

        public static final String NEW_USER_MUST_HAVE_MOD_STATE_NEW =
            "The new CCUser does not have ModState.NEW";

        public static final String USER_MUST_HAVE_MOD_STATE_CLEAN =
            "The new CCUser does not have ModState.CLEAN";
    }
}
