package by.ghoncharko.webproject.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateCreateOrUpdatePreparate {
    private static final Double MAX_PERMITTED_VALUE_FOR_DRUG_PRICE = 99999999999999.99d;
    private static final Integer MAX_PERMITTED_VALUE_FOR_DRUG_COUNT = Integer.MAX_VALUE;
    private ValidateCreateOrUpdatePreparate(){
    }

    public boolean validate(String drugName, Double drugPrice, Integer drugCount, String drugDescription, String drugProducerName, Boolean drugNeedRecipe){
        if(drugName != null && drugPrice != null && drugCount !=null && drugDescription !=null && drugProducerName!=null && drugNeedRecipe!=null){
            Pattern pattern = Pattern.compile(".{1,45}");
            Matcher matcher = pattern.matcher(drugName);
            boolean isValidDrugName = matcher.find();
            boolean isValidDrugPrice = drugPrice>=0 && drugPrice<= MAX_PERMITTED_VALUE_FOR_DRUG_PRICE;
            boolean isValidDrugCount = drugCount>=0 && drugCount<=MAX_PERMITTED_VALUE_FOR_DRUG_COUNT;
            boolean isValidDrugDescription = drugDescription.length()>0;
            boolean isValidDrugProducerName = drugProducerName.length()>0 && drugProducerName.length()<=45;
            return isValidDrugName && isValidDrugPrice && isValidDrugCount && isValidDrugDescription && isValidDrugPrice && isValidDrugProducerName;
        }
        return false;
    }

    public static ValidateCreateOrUpdatePreparate getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder{
        private static final ValidateCreateOrUpdatePreparate INSTANCE = new ValidateCreateOrUpdatePreparate();
    }
}
