package com.rengu.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class test {

//	public static void main(String[] args) {
//		List<String> selectedFile = new ArrayList<>();
//		selectedFile.add("C:/Users/12160/Desktop/清单文件/清单文件/能力清单/Capability1.xml");
//		selectedFile.add("C:/Users/12160/Desktop/清单文件/清单文件/任务类.xml");
//		// TODO Auto-generated method stub
//		// 遍历导入的清单文件
//		for (String string : selectedFile) {
//			try {
//				File file = new File(string);
//				// 创建SAXReader对象
//				SAXReader saxReader = new SAXReader();
//				// 读取XML文件，获取Document对象
//				Document document = saxReader.read(file);
//				// 遍历根节点,并判断是什么类型清单
//				Element rootElement = document.getRootElement();
//				// 查询数据库表的Entity_id
//				List<String> selectId = DatabaseUtils.selectId();
//
//				// 如果这个xml是任务清单,任务清单没有owner
//				if (rootElement.getName().equals("missionInventory")) {
//					// 拿到任务清单中所有mission元素
//					Element missionNode = (Element) document.selectSingleNode("//mission");
//					if (missionNode != null) {
//						String missionId = missionNode.attributeValue("id");// 代表entity_id
//						String missionName = missionNode.attributeValue("name");
//
//						insertIntoAttAndValue(missionNode, missionId);
//
//						if (selectId.size() != 0) {
//							for (String id : selectId) {
//								if (!id.equals(missionId)) {
//									// 插入实体数据
//									DatabaseUtils.insertEntity(missionId, missionName, "mission");
//								}
//							}
//						} else {
//							// 插入实体数据
//							DatabaseUtils.insertEntity(missionId, missionName, "mission");
//						}
//						// 拿到mission下的活动
//						List<Element> elements = missionNode.elements("activity");
//						for (Element actElement : elements) {
//							String actId = actElement.attributeValue("id");
//							String actName = actElement.attributeValue("name");
//
//							insertIntoAttAndValue(actElement, actId);
//
//							if (selectId.size() != 0) {
//								for (String id : selectId) {
//									if (!id.equals(actId)) {
//										// 插入实体数据
//										DatabaseUtils.insertEntity(actId, actName, "activity");
//									}
//								}
//							} else {
//								DatabaseUtils.insertEntity(actId, actName, "activity");
//							}
//							// 插入关系数据
//							String relationshipId = UUID.randomUUID().toString();
//							DatabaseUtils.insertRelationship(relationshipId, "关联", missionId, actId);
//						}
//
//					} else {
//						throw new RuntimeException("未找到匹配节点");
//					}
//					// 找到所有的作战概念
//					List<Element> metanodes = document.selectNodes("//metanode");
//					if (metanodes != null) {
//						for (Element element : metanodes) {
//							String metanodeId = element.attributeValue("id");
//							String metanodeName = element.attributeValue("name");
//							DatabaseUtils.insertEntity(metanodeId, metanodeName, "metanode");
//
//							insertIntoAttAndValue(element, metanodeId);
//
//							List<Element> systems = element.elements();
//							for (Element system : systems) {
//								String systemName = system.attributeValue("name");
//								String systemId = system.attributeValue("id");
//								DatabaseUtils.insertEntity(systemId, systemName, "metanode");
//
//								insertIntoAttAndValue(system, systemId);
//								// 插入关系数据
//								String relationshipId = UUID.randomUUID().toString();
//								DatabaseUtils.insertRelationship(relationshipId, "关联", metanodeId, systemId);
//							}
//						}
//					} else {
//						throw new RuntimeException("未找到匹配节点");
//					}
//				}
//				// 如果是能力清单
//				if (rootElement.getName().equals("capabilityInventory")) {
//					// 遍历右侧树选中的能力
//					Element root = document.getRootElement();
//					String owner = root.attributeValue("owner");
//					// 根据选中的能力名称在能力清单中查到元素
//					List<Element> selectNodes = document.selectNodes("//capability");
//					for (Element element : selectNodes) {
//						String capId = element.attributeValue("id");
//						String capName = element.attributeValue("name");
//
//						if (selectId.size() != 0) {
//							for (String id : selectId) {
//								if (!id.equals(capId)) {
//									// 插入实体数据
//									DatabaseUtils.insertEntity(capId, capName, "capability");
//								}
//							}
//						} else {
//							DatabaseUtils.insertEntity(capId, capName, "capability");
//						}
//
//						insertIntoAttAndValue(element, capId);
//
//						// 插入关系数据
//						String relationshipId = UUID.randomUUID().toString();
//						DatabaseUtils.insertRelationship(relationshipId, "关联", owner, capId);
//					}
//				}
//				// 如果是装备清单
//				if (rootElement.getName().equals("equipmentInventory")) {
//					Element root = document.getRootElement();
//					String owner = root.attributeValue("owner");
//					// 根据选中的装备名称在装备清单中查到元素
//					List<Element> selectNodes = document.selectNodes("//system");
//					for (Element element : selectNodes) {
//						String systemId = element.attributeValue("id");
//						String systemName = element.attributeValue("name");
//
//						if (selectId.size() != 0) {
//							for (String id : selectId) {
//								if (!id.equals(systemId)) {
//									// 插入实体数据
//									DatabaseUtils.insertEntity(systemId, systemName, "system");
//								}
//							}
//						} else {
//							DatabaseUtils.insertEntity(systemId, systemName, "system");
//						}
//
//						insertIntoAttAndValue(element, systemId);
//
//						// 插入关系数据
//						String relationshipId = UUID.randomUUID().toString();
//						DatabaseUtils.insertRelationship(relationshipId, "关联", owner, systemId);
//
//						// 拿到system的所有function
//						List<Element> functions = element.elements("function");
//						for (Element function : functions) {
//							String functionId = function.attributeValue("id");
//							String functionName = function.attributeValue("name");
//
//							if (selectId.size() != 0) {
//								for (String id : selectId) {
//									if (!id.equals(systemId)) {
//										// 插入实体数据
//										DatabaseUtils.insertEntity(functionId, functionName, "function");
//									}
//								}
//							} else {
//								DatabaseUtils.insertEntity(functionId, functionName, "function");
//							}
//
//							insertIntoAttAndValue(function, functionId);
//
//							// 插入关系数据
//							String relationshipId1 = UUID.randomUUID().toString();
//							DatabaseUtils.insertRelationship(relationshipId1, "关联", systemId, functionId);
//						}
//
//					}
//
//				}
//				// 如果是活动清单
//				if (rootElement.getName().equals("activityInventory")) {
//					// 遍历右侧树选中的能力
//					Element root = document.getRootElement();
//					String owner = root.attributeValue("owner");
//					// 根据选中的能力名称在能力清单中查到元素
//					List<Element> selectNodes = document.selectNodes("//activity");
//					for (Element element : selectNodes) {
//						String actId = element.attributeValue("id");
//						String actName = element.attributeValue("name");
//
//						if (selectId.size() != 0) {
//							for (String id : selectId) {
//								if (!id.equals(actId)) {
//									// 插入实体数据
//									DatabaseUtils.insertEntity(actId, actName, "activity");
//								}
//							}
//						} else {
//							DatabaseUtils.insertEntity(actId, actName, "activity");
//						}
//
//						insertIntoAttAndValue(element, actId);
//
//						// 插入关系数据
//						String relationshipId = UUID.randomUUID().toString();
//						DatabaseUtils.insertRelationship(relationshipId, "关联", owner, actId);
//					}
//
//				}
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
//		}
//	}

//	public static void insertIntoAttAndValue(Element element, String id) {
//		// 拿到所有属性
//		List<Attribute> attributes = element.attributes();
//		for (Attribute attribute : attributes) {
//			String attributeName = attribute.getName();
//			if (!attributeName.equals("id") && !attributeName.equals("name")) {
//				String attributeValue = attribute.getValue();
//				String replaceName = attributeName.replace("-", " ");
//				// 插入属性数据
//				String attributeId = UUID.randomUUID().toString();
//				DatabaseUtils.insertAttribute(attributeId, replaceName);
//				// 插入value数据
//				DatabaseUtils.insertValue(UUID.randomUUID().toString(), id, attributeId, attributeValue);
//			}
//
//		}
//	}

	//    public static void main(String[] args) {
//        String a = "{\n" +
//                "    \"id\": \"f2816cf2-7ebe-4f4a-a9fb-6b02a9360dab\",\n" +
//                "    \"createTime\": \"2023-08-17 23:03:13\",\n" +
//                "    \"code\": 0,\n" +
//                "    \"message\": \"请求成功\",\n" +
//                "    \"data\": [\n" +
//                "      [\n" +
//                "        {\n" +
//                "          \"system\": {\n" +
//                "            \"entityName\": \"name\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"4\",\n" +
//                "                \"entityName\": \"name\",\n" +
//                "                \"entityType\": \"function\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"capabilities\": {\n" +
//                "            \"entityName\": \"id\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"11\",\n" +
//                "                \"entityName\": \"id\",\n" +
//                "                \"entityType\": \"activity\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"boolList\": [\n" +
//                "            [\n" +
//                "              false\n" +
//                "            ]\n" +
//                "          ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"system\": {\n" +
//                "            \"entityName\": \"name\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"4\",\n" +
//                "                \"entityName\": \"name\",\n" +
//                "                \"entityType\": \"function\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"capabilities\": {\n" +
//                "            \"entityName\": \"name\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"12\",\n" +
//                "                \"entityName\": \"name\",\n" +
//                "                \"entityType\": \"activity\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"boolList\": [\n" +
//                "            [\n" +
//                "              false\n" +
//                "            ]\n" +
//                "          ]\n" +
//                "        }\n" +
//                "      ],\n" +
//                "      [\n" +
//                "        {\n" +
//                "          \"system\": {\n" +
//                "            \"entityName\": \"id\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"3\",\n" +
//                "                \"entityName\": \"id\",\n" +
//                "                \"entityType\": \"function\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"capabilities\": {\n" +
//                "            \"entityName\": \"id\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"11\",\n" +
//                "                \"entityName\": \"id\",\n" +
//                "                \"entityType\": \"activity\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"boolList\": [\n" +
//                "            [\n" +
//                "              false\n" +
//                "            ]\n" +
//                "          ]\n" +
//                "        },\n" +
//                "        {\n" +
//                "          \"system\": {\n" +
//                "            \"entityName\": \"id\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"3\",\n" +
//                "                \"entityName\": \"id\",\n" +
//                "                \"entityType\": \"function\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"capabilities\": {\n" +
//                "            \"entityName\": \"name\",\n" +
//                "            \"entities\": [\n" +
//                "              {\n" +
//                "                \"entityId\": \"12\",\n" +
//                "                \"entityName\": \"name\",\n" +
//                "                \"entityType\": \"activity\"\n" +
//                "              }\n" +
//                "            ]\n" +
//                "          },\n" +
//                "          \"boolList\": [\n" +
//                "            [\n" +
//                "              false\n" +
//                "            ]\n" +
//                "          ]\n" +
//                "        }\n" +
//                "      ]\n" +
//                "    ]\n" +
//                "  }";
//        JSONObject json = JSONObject.parseObject(a);
//        JSONArray data = (JSONArray) json.get("data");
//        for (int i = 0; i < data.size(); i++) {
//            JSONArray o = (JSONArray) data.get(i);
//            for (int i1 = 0; i1 < o.size(); i1++) {
//                JSONObject o1 = (JSONObject) o.get(i1);
//
//                // todo:获取系统
//                JSONObject system = (JSONObject) o1.get("system");
//                String sysName = (String) system.get("entityName");//获取系统名称
//                System.out.println("系统名称：" + sysName);
//                //todo:获取系统能力
//                JSONArray entityJson = (JSONArray) system.get("entities");
//                for (int i2 = 0; i2 < entityJson.size(); i2++) {
//                    JSONObject i2json = (JSONObject) entityJson.get(i2);
//                    String functionName = (String) i2json.get("entityName");
//                    System.out.println("系统能力：" + functionName);
//                }
//
//                //todo--------------------------------------------------
//
//                //todo：获取能力
//                JSONObject capabilities = (JSONObject) o1.get("capabilities");
//                String capabilityName = (String) capabilities.get("entityName");
//                System.out.println("能力名称：" + capabilityName);
//
//                JSONArray capabilityJson = (JSONArray) capabilities.get("entities");
//                for (int i2 = 0; i2 < capabilityJson.size(); i2++) {
//                    JSONObject i2json = (JSONObject) capabilityJson.get(i2);
//                    String activityName = (String) i2json.get("entityName");
//                    System.out.println("活动名称：" + activityName);
//                }
//
//                //todo:----------------------------------------------------
//
//                //todo:获取关联关系
//                JSONArray boolList = (JSONArray) o1.get("boolList");
//                for (int i2 = 0; i2 < boolList.size(); i2++) {
//                    JSONArray o2 = (JSONArray) boolList.get(i2);
//                    for (int i3 = 0; i3 < o2.size(); i3++) {
//                        System.out.println("遍历关系关系：" + o2.get(i3));
//                    }
//                }
//                System.out.println("----------结束-----------");
//            }
//        }
//    }

}
