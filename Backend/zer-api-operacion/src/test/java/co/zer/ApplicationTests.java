package co.zer;

import co.zer.utils.Uuid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Test
    public void pruebas() {
        //System.out.println(Uuid.getUuidLong());
        //System.out.println(Uuid.getUuidTexto());
        Long minutosACobrar=60l;
        Long horas = Math.floorDiv(minutosACobrar, 60l);
        if(minutosACobrar%60l!=0){
            horas +=1;
        }
        System.out.println(horas);

        minutosACobrar=61l;
        horas = Math.floorDiv(minutosACobrar, 60l);
        if(minutosACobrar%60l!=0){
            horas +=1;
        }
        System.out.println(horas);

        minutosACobrar=1l;
        horas = Math.floorDiv(minutosACobrar, 60l);
        if(minutosACobrar%60l!=0){
            horas +=1;
        }
        System.out.println(horas);

        minutosACobrar=59l;
        horas = Math.floorDiv(minutosACobrar, 60l);
        if(minutosACobrar%60l!=0){
            horas +=1;
        }
        System.out.println(horas);
        minutosACobrar=179l;
        horas = Math.floorDiv(minutosACobrar, 60l);
        if(minutosACobrar%60l!=0){
            horas +=1;
        }
        System.out.println(horas);
    }

	/*
	@Test
	public void contextLoads() {

	}

	@Test
	public void generadorUUID() {
		System.out.println(Uuid.getUuidLong());
		System.out.println(Uuid.getUuidTexto());
	}

	@Test
	public void valorDiffie(){
		//System.out.println(Hash.getHashStringHexa("1234"));
		//DiffRespuesta diffRespuesta = AutenticacionService.getAuthInicial();
        long a= (long) (Math.random()*99999L);
        long b= (long) (Math.random()*99999L);
        System.out.println("a:"+a+" b:"+b);
        long p= (long) (Math.random()*99999L);System.out.println(AutenticacionService.moduloPotencia(9999,34,9999));
        long g= (long) (Math.random()*99999L);
        System.out.println("p:"+p+" g:"+g);
        long A = AutenticacionService.moduloPotencia(g,a,p);
        long B = AutenticacionService.moduloPotencia(g,b,p);
        System.out.println("A:"+a+" B:"+b);
        long s1 = AutenticacionService.moduloPotencia(B,a,p);
        long s2 = AutenticacionService.moduloPotencia(A,b,p);
        System.out.println("s1:"+s1+" s2:"+s2);

	}
*/
}
