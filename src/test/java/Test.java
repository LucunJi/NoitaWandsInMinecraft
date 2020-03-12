import io.github.lucunji.noitacraft.spell.ISpellEnum;
import io.github.lucunji.noitacraft.spell.ProjectileSpell;
import io.github.lucunji.noitacraft.util.SpellTree;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<ISpellEnum> testList = Arrays.asList(
                ProjectileSpell.SPARK_BOLT,
                ProjectileSpell.SPARK_BOLT_TRIGGER2,
                ProjectileSpell.SPARK_BOLT_TRIGGER2,
                ProjectileSpell.SPARK_BOLT,
                ProjectileSpell.SPARK_BOLT
                );
        Iterator i1 = testList.iterator();
        i1.next();
        SpellTree root = new SpellTree(i1, testList::iterator);
        System.out.println(root);
    }
}
