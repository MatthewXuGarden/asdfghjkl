/**
 * 
 */
package com.carel.supervisor.ide.dc.DbModel.Translation;

import java.util.HashMap;
import java.util.Set;

import com.carel.supervisor.ide.dc.xmlDAO.TranslationsTag;

/**
 * @author Utente
 *
 */
public class Translations {

	
	
	private HashMap<String, Language> languages;
	
	public Translations()
	{
		languages = new HashMap<String, Language>();
	}
	
	public void fillTranslations(TranslationsTag translationsTag)
	{
		translationsTag.getHmLang();
		
		Set<String> langSet = translationsTag.getHmLang().keySet();
		for(String lang:langSet){
			Language language = new Language();
			language.setKey(lang);
			Set<String> sectionSet = translationsTag.getHmLang().get(lang).getHmSection().keySet();
			for(String sectKey:sectionSet){
				Section section = new Section();
				section.setName(sectKey);
				Set<String> itemSet = translationsTag.getHmLang().get(lang).getHmSection().get(sectKey).getHmItem().keySet();
				for(String itemKey:itemSet){
					Item item = new Item();
					item.setCode(itemKey);
					Set<String> keySet = translationsTag.getHmLang().get(lang).getHmSection().get(sectKey).getHmItem().get(itemKey).getHmKey().keySet();
					for(String key:keySet){
						item.getKeys().put(key, translationsTag.getHmLang().get(lang).getHmSection().get(sectKey).getHmItem().get(itemKey).getHmKey().get(key).getValue());
					}
					section.getItems().put(item.getCode(), item);
				}
				language.getSections().put(section.getName(), section);
			}
			languages.put(language.getKey(), language);
		}
	}

	public HashMap<String, Language> getLanguages() {
		return languages;
	}

	public void setLanguages(HashMap<String, Language> languages) {
		this.languages = languages;
	}

}
