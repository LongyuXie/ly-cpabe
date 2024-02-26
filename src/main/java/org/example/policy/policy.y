
%language "Java"

%define api.prefix {PolicyParser}
%define api.parser.class {PolicyParser}
%define api.parser.public
%define api.value.type {ParserVal}
%define api.package {org.example.policy}
%define api.parser.implements
%define api.push-pull both


%code imports {
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.security.InvalidParameterException;
import java.util.List;


}

%code {
    public static void main(String[] args) throws IOException{
        var file = "C:\\Users\\xielongyu\\Desktop\\abe\\ly-cpabe\\src\\main\\resources\\input.txt";
        var stream = new FileInputStream(file);
        var policyLexer = new PolicyLexer(stream);
        var parser = new PolicyParser(policyLexer);
        parser.parse();
    }
}

%code {
    AccessTreeNode leaf_policy(String attr){
        AccessTreeNode p = new AccessTreeNode();
        p.setAttr(attr);
        p.setThreshold(Threshold.Companion.getONE());
        return p;
    }

    AccessTreeNode kof2_policy(int k, AccessTreeNode l, AccessTreeNode r){
        AccessTreeNode p = new AccessTreeNode();
        if (k == 1) {
            p.setThreshold(Threshold.Companion.getOR());
        } else if (k == 2) {
            p.setThreshold(Threshold.Companion.getAND());
        }
        p.getChildren().add(l);
        p.getChildren().add(r);
        return p;
    }

    AccessTreeNode kof_policy(int k, List<AccessTreeNode> list){
        AccessTreeNode p = new AccessTreeNode();
        var n = list.size();
        var th = new Threshold(k, n);
        if (k > n) {
            throw new InvalidParameterException("threshold " + th + " invalid!" );
        }
        p.setThreshold(new Threshold(k, n));
        p.getChildren().addAll(list);

        return p;
    }
    AccessTreeNode res;
}


%token  ATTR
%token  NUM

%left OR
%left AND
%token OF


%%

result: policy { res = (AccessTreeNode)((ParserVal)$1).obj; }

policy:   ATTR                       { $$.obj = leaf_policy($1.sval);        }
        | policy OR  policy          { $$.obj = kof2_policy(1, (AccessTreeNode)$1.obj, (AccessTreeNode)$3.obj); }
        | policy AND policy          { $$.obj = kof2_policy(2, (AccessTreeNode)$1.obj, (AccessTreeNode)$3.obj); }
        | NUM OF '(' arg_list ')'    { $$.obj = kof_policy($1.ival, (List<AccessTreeNode>)$4.obj);     }
        | '(' policy ')'             { $$ = $2;                     }

arg_list: policy                     { var obj = new ArrayList<AccessTreeNode>();
                                       obj.add((AccessTreeNode)$1.obj);
                                       $$.obj = obj;
                                       }
        | arg_list ',' policy        { $$ = $1;
                                       ((List<AccessTreeNode>)$$.obj).add((AccessTreeNode)$3.obj); }
;

%%