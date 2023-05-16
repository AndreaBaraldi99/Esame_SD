import java.util.ArrayList;
import java.util.List;

public class ContoCorrente {
    private int codiceConto;
    private List<Integer> codiceCliente = new ArrayList<Integer>();
    private double saldo = 0;

    public ContoCorrente(int[] codiceCliente) {
        this.codiceConto = (int) (Math.random() * 100000);
        for (int i = 0; i < codiceCliente.length; i++) {
            this.codiceCliente.add(codiceCliente[i]);
        }
    }

    public int getCodiceConto() {
        return codiceConto;
    }

    synchronized public boolean versa(double importo) {
        saldo += importo;
        return true;
    }

    synchronized public boolean preleva(double importo) {
        if(saldo - importo < 0)
            return false;
        saldo -= importo;
        return true;
    }
    
    public boolean getCliente(int codiceCliente){
        return this.codiceCliente.contains(codiceCliente);
    }
}
