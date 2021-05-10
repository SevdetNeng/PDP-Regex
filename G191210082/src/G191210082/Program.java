/**
*
* @author Sevdet IŞIK sevdet.isik@ogr.sakarya.edu.tr
* @since 08.04.2021
* <p>
* Bir C++ kaynak dosyasını okuyup,bu dosyada bulunan classları,super classları ve methodları,parametreleri düzenli bir şekilde
* konsol ekranına yazan program.
* </p>
*/



package G191210082;

import java.util.LinkedHashMap;

import java.util.Map.Entry;

import java.util.regex.*;

import java.io.*;


public class Program {

	public static void main(String[] args) throws IOException {
		
		
		
		//Program.cpp dosyası okunmak üzere file olarak tanımlanıyor.
		File file = new File("src//program.cpp");
		 
			try {
				//Program.cpp dosyası okunup,gerekli regexler patternlar aracılığıyla tanımlanıyor.
				BufferedReader br = new BufferedReader(new FileReader(file));
				String satir;
				String classRegex="(class|Class) ([a-zA-Z]+) ?+\\{?(?: ?\\:? ?)?(([a-zA-Z]+ ?[a-zA-Z]+ ?\\,? ?+\\{?)+)?";
				String superClassRegex="([a-zA-Z0-9]+) +?\\{";
				String metodRegex=" ?+(?!\\;)(?!switch)(?!for)(?!while)(?!if)(?!else if)(public|private|protected|friend|const)? ?+([a-zA-Z&*~>\\-<!=]+)? ?+([a-zA-Z&\\->*~<()!=\\[\\]]+)?( )?+\\(([a-zA-Z0-9*&,<> \\=\\-~()\\[\\]!]+)?\\)(?:const)? ?(?:\\:)?([a-zA-Z0-9*&,<> \\\\=\\\\-~\\[\\]]+)?\\(?(?:[a-zA-Z\\*\\&\\,\\[\\]\\ \\-\\>\\<\\(\\)]+)?\\)?\\{?";
				Pattern metodPattern = Pattern.compile(metodRegex);
				Pattern classPattern = Pattern.compile(classRegex);
				Pattern superClassPattern=Pattern.compile(superClassRegex);
				
				String[] superClass1=new String[50];
				String sc = null;
				
				
				//Super sınıfların iki çeşit değişkenini tutmak için HashMap tanımlanıyor.
				LinkedHashMap<String, Integer> scMap = new LinkedHashMap<String, Integer>();
												
				int publicSayac=0;
				
				//Program.cpp satır satır okunuyor.
				while ((satir = br.readLine()) != null) {
					//Okunan satırlar fazlalık boşluklardan arındırıldıktan sonra regexler ile kontrol ediliyor.
					satir=satir.trim();
					satir=satir.replaceAll(" +", " ");
					Matcher matcherClass = classPattern.matcher(satir);
					Matcher matcherMetod = metodPattern.matcher(satir);
					
					
					//Eğer class regexi true döndürürse if bloğuna giriliyor.
					if (matcherClass.matches()) {
						
						//Regex üzerinden sınıf ismi alınıyor.
						publicSayac=0;
					    String isim=matcherClass.group(2);
					    
					    //Sınıf ismi ekrana yazdırılıyor.
					    System.out.println("Sinif:"+isim);
					    
					    //Eğer satırda ":" karakteri bulunuyorsa if bloğuna giriliyor(Super sınıfları analiz edebilmek için)
					    if(satir.contains(":")) {
					    	//Eğer class regexinin döndürdüğü grup 3 null değilse if bloğuna giriliyor.
					    	if(matcherClass.group(3)!=null) {
						    	sc=matcherClass.group(3);
						    }						    
						    
						    //Eğer sc değişken değeri null değilse sc değeri virgüllerden itibaren bölünüp,çıkan değerler string arraye aktarılıyor.
						    if(sc!=null)
						    superClass1=sc.split(",");
						    for(int i=0;i<superClass1.length;i++) {
						    	//Virgüller ile ayrılan tüm değerler taranıyor.
						    	if(superClass1[i]!=null) {
						    		
						    		//Super Class ismini almak için gerekli işlemler yapılıyor.
						    		superClass1[i]=superClass1[i].trim();
						    		Matcher matcherSuperClass=superClassPattern.matcher(superClass1[i]);
						    		if(matcherSuperClass.matches()) {
						    			String sClass=matcherSuperClass.group(1);
						    				
						    			if(!scMap.containsKey(sClass)) {
						    				scMap.put(sClass, 1);
						    			}
						    			else {
						    				scMap.put(sClass, scMap.get(sClass)+1);
						    			}
						    		}
						    		else {
						    			String sClass = superClass1[i].substring(superClass1[i].indexOf(' ') + 1);
								    	if(sClass.contains("{")) 
								    		sClass=sClass.replace("{", "");
								    		
								    		
								    	sClass=sClass.replaceAll(" +", " ");
								    	sClass.trim();
								    	if(!scMap.containsKey(sClass)) {
						    				scMap.put(sClass, 1);
						    			}
						    			else {
						    				scMap.put(sClass, scMap.get(sClass)+1);
						    			}
								    		
						    		}							    									    							    								    								    									    		
						    	}
						    	
						    }
					    }
					    
					    
					    
					}
					//Daha önceki okunan satırlarda public: satırının gelip gelmediği kontrol ediliyor.
					if(satir.equals("public:"))
					{
						publicSayac=1;
					}
					//Eğer public'ten sonra protected veya private satırı gelirse değişken değeri tekrardan 0'a getiriliyor.
					if(satir.equals("protected:"))
					{
						publicSayac=0;
					}
					if(satir.equals("private:"))
					{						
						publicSayac=0;
					}
					
					//Eğer regex ile satır değeri eşleşirse if bloğuna giriliyor.
					if(matcherMetod.matches()) {
						
						//Eğer öncesinde public: satırı geldiyse,veya metod isminin önünde public varsa if bloğuna giriliyor.
						if("public".equals(matcherMetod.group(1)) || publicSayac==1) {
							
							//Eğer elimize gelen metod main ise işlem yapılmıyor.
							if(!satir.contains("int main(")) {
								String ozelKarakter;
								
								//Eğer regex'ten dönüt olan grup 3 null değilse if bloğuna giriliyor.
								if(matcherMetod.group(3)!=null) {
									String metod=matcherMetod.group(3);
									String donus=matcherMetod.group(2);
									
									//Metodun dönüş tipinin sonunda bulunan özel karakterler metod ismine yazılmışsa düzeltmeler yapılıyor.
									if(metod.startsWith("&*")) {
										ozelKarakter="&*";
										metod=metod.substring(2);
										donus=donus+ozelKarakter;
									}
									else if(metod.startsWith("&")) {
										ozelKarakter="&";
										metod=metod.substring(1);
										donus=donus+ozelKarakter;
									}
									
									else if(metod.startsWith("*&")) {
										ozelKarakter="*&";
										metod=metod.substring(2);
										donus=donus+ozelKarakter;
									}
									
									else if(metod.startsWith("*")) {
										ozelKarakter="*";
										metod=metod.substring(1);
										donus=donus+ozelKarakter;
									}
									//Metod ismi ekrana yazılıyor.
									System.out.println("	"+metod);
									
									//Dönüş türü ekrana yazılıyor.
									System.out.println("		Donus Turu "+donus);
								}
								else {
									String metod=matcherMetod.group(2);
									System.out.println("	"+metod);
									System.out.println("		Donus Turu:Nesne Adresi");
								}
																									
								String parametre=matcherMetod.group(5);
								String[] parametre2=null;
								String[] parametre3=null;
								String[] parametreler=new String[100];
									
									//Eğer metod parametre alıyorsa if bloğuna giriliyor.
								if(parametre!=null) {
										
									//parametrelere gerekli fazlalık boşluk silme işlemleri yapılıyor.
									parametre=parametre.trim();
									parametre=parametre.replaceAll(" +", " ");
										
									//Parametreler aralarındaki virgüllerden itibaren kesilip,bir string array'ine aktarılıyor.
									parametre2=parametre.split(",");
										
									//Bütün parametre elemanları taranıyor.
									for(int i=0;i<parametre2.length;i++) {
											
										//Parametreler ve parametre veri tipleri boşluklardan itibaren ayrılıyor.
										parametre3=parametre2[i].split(" ");
										if(parametre3[0]==" ")
												
										for(int j=0;j<parametre3.length;j++)
												parametre3[j].trim();
												
													
										int esitSayac=0;	
										int esitIndex=0;
														
										//Parametre içerisinde "=" karakteri olup olmadığı kontrol ediliyor.
										for(int j=0;j<parametre3.length;j++) {
											
										//Eğer "=" karakteri varsa if bloğuna giriliyor.
												if(parametre3[j].equals("="))
												{
													esitIndex=j;
													esitSayac++;
												}
											}
														
										//Eğer "=" karakteri varsa aşağıdaki tanımlama yapılıyor.
													
										if(esitSayac==1) {
											parametreler[i]=parametre3[esitIndex-2];
										}
														
										//Eğer "=" karakteri yoksa aşağıdaki tanımlama yapılıyor.
										else {
															
											parametreler[i]=parametre3[parametre3.length-2];
										}
									}
										
						
									int parametreSayac=0;
									for(int i=0;i<parametreler.length;i++) {
										if(parametreler[i]!=null) {
											parametreSayac++;
										}
									}
									//Parametre sayısı ekrana yazılıyor.
									System.out.print("		Parametre:"+parametreSayac+" (");
										
									//Parametreler sırasıyla ekrana yazılıyor.
									for(int i=0;i<parametreler.length;i++) {
										if(parametreler[i]!=null) {
											if(parametreler[i+1]!=null)
											System.out.print(parametreler[i]+",");
											else if(parametreler[i+1]==null)
												System.out.println(parametreler[i]+")");
													
										}
									}			
								}
								else {
									//Parametre bulunmuyorsa "Parametre:0" ekrana yazılıyor.
									System.out.println("		Parametre:0");
								}
									
							}
							
						}
						
					}
					
				}
				//Super sınıflar ekrana yazılıyor.
				System.out.println("Super Siniflar:");
				for(Entry<String, Integer> it:scMap.entrySet()) {
					System.out.println("	"+it.getKey()+":"+it.getValue());
					
				}
			//Okunan dosya kapanıyor.	
			br.close();
			
			//Eğer dosya açma işlemi başarısız olursa ekrana "dosya acilamadi" yazılıyor.
			} catch (FileNotFoundException e) {
				System.out.println("dosya acilamadi");
			}
			
	}

}

