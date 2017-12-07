package no.uib.ii.algo.st8;

import no.uib.ii.algo.st8.model.DefaultVertex;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    DefaultVertex.resetCounter();

    System.out.println("Launching workspace");
    Intent ws = new Intent(this, Workspace.class);

    //EDITED
    Bundle bundle = getIntent().getExtras();
    long boothNo = bundle.getLong("BOOTH");
    String mode = bundle.getString("MODE");
    long catId = bundle.getLong("CATEGORY");

    Bundle b = new Bundle();
    b.putLong("BOOTH", boothNo);
    b.putString("MODE", mode);
    b.putLong("CATEGORY", catId);

    ws.putExtras(b);

    startActivityForResult(ws, 1397);

    System.out.println("MAIN main main");
    System.out.println("finish?");
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == 1397 && resultCode == 0)
      finish();
  }
}
