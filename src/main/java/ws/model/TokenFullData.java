package ws.model;
public class TokenFullData extends Token {
	Client client = null;

	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
}