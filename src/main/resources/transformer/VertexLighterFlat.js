function initializeCoreMod() {
    return {
        'coremodone': {
            'target': {
                'type': 'CLASS',
                'name': 'net.minecraftforge.client.model.pipeline.VertexLighterFlat'
            },
            'transformer': function (classNode) {
                var Opcodes = Java.type("org.objectweb.asm.Opcodes");

                var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
                var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");
                var InsnNode = Java.type("org.objectweb.asm.tree.InsnNode");
                var LdcInsnNode = Java.type("org.objectweb.asm.tree.LdcInsnNode");
                var FieldInsnNode = Java.type("org.objectweb.asm.tree.FieldInsnNode");
                
                var InsnList = Java.type("org.objectweb.asm.tree.InsnList");

                var api = Java.type('net.minecraftforge.coremod.api.ASMAPI');

                var methods = classNode.methods;

                for (m in methods) 
                {
                    var method = methods[m];
                    if (method.name === "processQuad") 
                    {
                    	var insnList = new InsnList();
                    	
                    	insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    	insnList.add(new VarInsnNode(Opcodes.ILOAD, 8));
                    	insnList.add(new InsnNode(Opcodes.AALOAD));
                    	insnList.add(new InsnNode(Opcodes.ICONST_0));
                    	
                    	insnList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                    	insnList.add(new VarInsnNode(Opcodes.ILOAD, 8));
                    	insnList.add(new InsnNode(Opcodes.AALOAD));
                    	insnList.add(new InsnNode(Opcodes.ICONST_0));
                    	insnList.add(new InsnNode(Opcodes.FALOAD));
                    	
                    	insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    	insnList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraftforge/client/model/pipeline/VertexLighterFlat", "tint", "I"));
                    	
                    	insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "lumien/randomthings/asm/AsmHandler", "modBlockLight", "(FI)F"));
						
						insnList.add(new InsnNode(Opcodes.FASTORE));
						
                    	api.insertInsnList(method, api.MethodType.VIRTUAL, "net/minecraftforge/client/model/pipeline/VertexLighterFlat", "updateColor", "([F[FFFFFI)V", insnList,api.InsertMode.INSERT_AFTER);
                        break;
                    }
                }

                return classNode;
            }
        }
    }
}